package farandula

import (
	"fmt"
	"strings"
	"unicode"
	"unicode/utf8"
)

type tokenType int

const (
	tokenError tokenType = iota // error occurred; value is text of error
	tokenEOF
	tokenCabin
	tokenNumber
	tokenTraveler
	tokenAirportCode
	tokenDate
	tokenKeyword   // used just to delimit keywords
	tokenSearch    // SEARCH keyword
	tokenFlights   // FLIGHTS keyword
	tokenFor       // FOR keyword
	tokenIn        // IN keyword
	tokenComma     // COMMA keyword
	tokenAnd       // AND keyword
	tokenFrom      // FROM keyword
	tokenTo        // TO keyword
	tokenDeparting // DEPARTING keyword
	tokenThen      // THEN keyword
	tokenReturning // RETURNING keyword
)

var tokenName = map[tokenType]string{
	tokenError:       "error",
	tokenEOF:         "EOF",
	tokenSearch:      "SEARCH",
	tokenFlights:     "FLIGHTS",
	tokenFor:         "FOR",
	tokenIn:          "IN",
	tokenComma:       ",",
	tokenAnd:         "AND",
	tokenFrom:        "FROM",
	tokenTo:          "TO",
	tokenDeparting:   "DEPARTING",
	tokenThen:        "THEN",
	tokenReturning:   "RETURNING",
	tokenCabin:       "cabin",
	tokenNumber:      "number",
	tokenTraveler:    "passenger",
	tokenAirportCode: "airport-code",
	tokenDate:        "date",
}

func (i tokenType) String() string {
	s := tokenName[i]
	if s == "" {
		return fmt.Sprintf("token%d", int(i))
	}
	return s
}

type token struct {
	typ  tokenType
	pos  int
	val  string
	line int
}

func (i token) String() string {
	switch {
	case i.typ == tokenEOF:
		return "EOF"
	case i.typ == tokenError:
		return i.val
	case i.typ > tokenKeyword:
		return fmt.Sprintf("<%s>", i.val)
	case len(i.val) > 10:
		return fmt.Sprintf("%.10q...", i.val)
	}
	return fmt.Sprintf("%q", i.val)
}

var key = map[string]tokenType{
	"search":    tokenSearch,
	"flights":   tokenFlights,
	"for":       tokenFor,
	"in":        tokenIn,
	",":         tokenComma,
	"and":       tokenAnd,
	"from":      tokenFrom,
	"to":        tokenTo,
	"departing": tokenDeparting,
	"then":      tokenThen,
	"returning": tokenReturning,
}

var enum = map[string]tokenType{
	"Economy":  tokenCabin,
	"Premium":  tokenCabin,
	"First":    tokenCabin,
	"Business": tokenCabin,
	"Coach":    tokenCabin,
	"Class":    tokenCabin,
	"Other":    tokenCabin,
	"Adult":    tokenTraveler,
	"Child":    tokenTraveler,
	"Infant":   tokenTraveler,
	"Senior":   tokenTraveler,
	"Adults":   tokenTraveler,
	"Children": tokenTraveler,
	"Infants":  tokenTraveler,
	"Seniors":  tokenTraveler,
}

const eof = -1

type stateFn func(*queryLexer) stateFn

type queryLexer struct {
	name    string     // the name of the input; used only for error reports
	input   string     // the string being scanned
	state   stateFn    // the nextRune lexing function to enter
	pos     int        // current position in the input
	start   int        // start position of this token
	width   int        // width of last rune read from input
	lastPos int        // position of most recent token returned by nextToken
	tokens  chan token // channel of scanned tokens
	line    int        // 1+number of lines seen
}

func newQueryLexer(name, input string) *queryLexer {
	l := &queryLexer{
		name:   name,
		input:  input,
		tokens: make(chan token),
		line:   1,
	}
	go l.run()
	return l
}

// returns the nextToken item from the input.
func (l *queryLexer) nextToken() token {
	tok := <-l.tokens
	l.lastPos = tok.pos
	return tok
}

func (l *queryLexer) run() {
	for l.state = lexTopLevel; l.state != nil; {
		l.state = l.state(l)
	}
	close(l.tokens)
}

func lexTopLevel(l *queryLexer) stateFn {
	l.width = 0
	r := l.nextRune()

	switch {
	case r == eof:
		l.emitToken(tokenEOF)
		return nil
	case isSpace(r):
		return lexSpace
	case r == ',':
		l.emitToken(tokenComma)
		return lexTopLevel
	case unicode.IsDigit(r):
		l.backupRune()
		return lexNumeric
	case isAlphaNumeric(r):
		l.backupRune()
		return lexConstituent
	default:
		return l.errorf("unrecognized character in query: %#U", r)
	}
	return nil
}

func lexSpace(l *queryLexer) stateFn {
	l.acceptRun(" \t\r\n")
	l.ignore()
	return lexTopLevel
}

func lexConstituent(l *queryLexer) stateFn {
	for {
		if r := l.nextRune(); !isAlphaNumeric(r) {
			l.backupRune()
			word := l.input[l.start:l.pos]
			switch {
			case len(word) == 3 && allCaps(word):
				l.emitToken(tokenAirportCode)
			case key[word] > tokenKeyword:
				l.emitToken(key[word])
			case enum[word] < tokenKeyword:
				l.emitToken(enum[word])
			default:
				l.errorf("unrecognized constituent in query: %s", word)
			}
			break
		}
	}
	return lexTopLevel
}

func lexNumeric(l *queryLexer) stateFn {
	if !l.scanNumber() {
		return l.errorf("bad number syntax: %q", l.input[l.start:l.pos])
	}
	if div1 := l.peekRune(); div1 == '-' || div1 == '/' {
		l.accept("-/")
		if !l.scanNumber() {
			return l.errorf("bad date syntax: %q", l.input[l.start:l.pos])
		}
		if div2 := l.peekRune(); div2 == div1 {
			l.accept(string(div1))
			if !l.scanNumber() {
				return l.errorf("bad date syntax: %q", l.input[l.start:l.pos])
			}
			l.emitToken(tokenDate)
		} else {
			return l.errorf("bad date syntax: %q", l.input[l.start:l.pos])
		}
	} else {
		l.emitToken(tokenNumber)
	}
	return lexTopLevel
}

func (l *queryLexer) scanNumber() bool {
	digits := "0123456789"
	l.acceptRun(digits)
	if isAlphaNumeric(l.peekRune()) {
		l.nextRune()
		return false
	}
	return true
}

func isSpace(r rune) bool {
	return r == ' ' || r == '\t' || r == '\r' || r == '\n'
}

func isAlphaNumeric(r rune) bool {
	return unicode.IsLetter(r) || unicode.IsDigit(r)
}

func allCaps(word string) bool {
	for _, r := range word {
		if !(unicode.IsLetter(r) && unicode.IsUpper(r)) {
			return false
		}
	}
	return true
}

// returns the nextRune rune in the input
func (l *queryLexer) nextRune() rune {
	if l.pos >= len(l.input) {
		l.width = 0
		return eof
	}
	r, w := utf8.DecodeRuneInString(l.input[l.pos:])
	l.width = w
	l.pos += l.width
	if r == '\n' {
		l.line++
	}
	return r
}

// returns but does not consume the nextRune rune in the input
func (l *queryLexer) peekRune() rune {
	r := l.nextRune()
	l.backupRune()
	return r
}

// steps back one rune. Can only be called once per call of nextRune
func (l *queryLexer) backupRune() {
	l.pos -= l.width
	if l.width == 1 && l.input[l.pos] == '\n' {
		l.line--
	}
}

// passes an token to the channel
func (l *queryLexer) emitToken(t tokenType) {
	l.tokens <- token{t, l.start, l.input[l.start:l.pos], l.line}
	// If some tokens could contain text with newlines internally, we would have to
	// count the newlines into l.line:
	// l.line += strings.Count(l.input[l.start:l.pos], "\n")
	l.start = l.pos
}

// skips over the pending input before this point
func (l *queryLexer) ignore() {
	l.start = l.pos
}

// consumes the nextRune rune if it's from the valid set
func (l *queryLexer) accept(valid string) bool {
	if strings.ContainsRune(valid, l.nextRune()) {
		return true
	}
	l.backupRune()
	return false
}

// consumes a run of runes from the valid set
func (l *queryLexer) acceptRun(valid string) {
	for strings.ContainsRune(valid, l.nextRune()) {
	}
	l.backupRune()
}

// returns an error token and terminates the scan by passing back a nil pointer
// that will be the nextToken state, terminating l.nextToken
func (l *queryLexer) errorf(format string, args ...interface{}) stateFn {
	l.tokens <- token{tokenError, l.start, fmt.Sprintf(format, args...), l.line}
	return nil
}
