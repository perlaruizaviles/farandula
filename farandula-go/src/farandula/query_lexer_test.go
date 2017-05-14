package farandula

import (
	"testing"
)

type lexTest struct {
	name   string
	input  string
	tokens []token
}

func mToken(typ tokenType, text string) token {
	return token{
		typ: typ,
		val: text,
	}
}

var (
	tEOF       = mToken(tokenEOF, "")
	tSearch    = mToken(tokenSearch, "search")
	tFlights   = mToken(tokenFlights, "flights")
	tFor       = mToken(tokenFor, "for")
	tIn        = mToken(tokenIn, "in")
	tComma     = mToken(tokenComma, ",")
	tAnd       = mToken(tokenAnd, "and")
	tFrom      = mToken(tokenFrom, "from")
	tTo        = mToken(tokenTo, "to")
	tDeparting = mToken(tokenDeparting, "departing")
	tThen      = mToken(tokenThen, "then")
	tReturning = mToken(tokenReturning, "returning")
)

var lexTests = []lexTest{
	{"empty", "", []token{tEOF}},
	{"spaces", " \t\n", []token{tEOF}},
	{"keyword search", "search", []token{tSearch, tEOF}},
	{"keyword flights", "flights", []token{tFlights, tEOF}},
	{"keyword for", "for", []token{tFor, tEOF}},
	{"keyword in", "in", []token{tIn, tEOF}},
	{"keyword comma", ",", []token{tComma, tEOF}},
	{"keyword and", "and", []token{tAnd, tEOF}},
	{"keyword from", "from", []token{tFrom, tEOF}},
	{"keyword to", "to", []token{tTo, tEOF}},
	{"keyword departing", "departing", []token{tDeparting, tEOF}},
	{"keyword then", "then", []token{tThen, tEOF}},
	{"keyword returning", "returning", []token{tReturning, tEOF}},
	{"number", "2", []token{mToken(tokenNumber, "2"), tEOF}},
	{"big number", "123456", []token{mToken(tokenNumber, "123456"), tEOF}},
	{"cabin economy", "Economy", []token{mToken(tokenCabin, "Economy"), tEOF}},
	{"cabin premium", "Premium", []token{mToken(tokenCabin, "Premium"), tEOF}},
	{"cabin first", "First", []token{mToken(tokenCabin, "First"), tEOF}},
	{"cabin business", "Business", []token{mToken(tokenCabin, "Business"), tEOF}},
	{"cabin coach", "Coach", []token{mToken(tokenCabin, "Coach"), tEOF}},
	{"cabin class", "Class", []token{mToken(tokenCabin, "Class"), tEOF}},
	{"cabin other", "Other", []token{mToken(tokenCabin, "Other"), tEOF}},
	{"passenger adult", "Adult", []token{mToken(tokenTraveler, "Adult"), tEOF}},
	{"passenger child", "Child", []token{mToken(tokenTraveler, "Child"), tEOF}},
	{"passenger infant", "Infant", []token{mToken(tokenTraveler, "Infant"), tEOF}},
	{"passenger senior", "Senior", []token{mToken(tokenTraveler, "Senior"), tEOF}},
	{"passenger adult", "Adults", []token{mToken(tokenTraveler, "Adults"), tEOF}},
	{"passenger child", "Children", []token{mToken(tokenTraveler, "Children"), tEOF}},
	{"passenger infant", "Infants", []token{mToken(tokenTraveler, "Infants"), tEOF}},
	{"passenger senior", "Seniors", []token{mToken(tokenTraveler, "Seniors"), tEOF}},
	{"airport code", "LHR", []token{mToken(tokenAirportCode, "LHR"), tEOF}},
	{"airport code", "DFW", []token{mToken(tokenAirportCode, "DFW"), tEOF}},
	{"airport code", "CDG", []token{mToken(tokenAirportCode, "CDG"), tEOF}},
	{"date", "2017-07-09", []token{mToken(tokenDate, "2017-07-09"), tEOF}},
	{"date", "2017/07/09", []token{mToken(tokenDate, "2017/07/09"), tEOF}},
	{"date", "2017/7/09", []token{mToken(tokenDate, "2017/7/09"), tEOF}},
	{"date", "2017-12-9", []token{mToken(tokenDate, "2017-12-9"), tEOF}},
	{
		"one way",
		`search flights from DFW to CDG departing 2017-07-09 for 1 Adult in Economy`,
		[]token{
			tSearch, tFlights, tFrom, mToken(tokenAirportCode, "DFW"), tTo, mToken(tokenAirportCode, "CDG"),
			tDeparting, mToken(tokenDate, "2017-07-09"), tFor, mToken(tokenNumber, "1"),
			mToken(tokenTraveler, "Adult"), tIn, mToken(tokenCabin, "Economy"), tEOF,
		},
	},
	{
		"round trip",
		`search flights from DFW to CDG departing 2017-07-09 then returning 2017-07-11 for
		2 Adults and 3 Children in Economy`,
		[]token{
			tSearch, tFlights, tFrom, mToken(tokenAirportCode, "DFW"), tTo, mToken(tokenAirportCode, "CDG"),
			tDeparting, mToken(tokenDate, "2017-07-09"), tThen, tReturning, mToken(tokenDate, "2017-07-11"), tFor,
			mToken(tokenNumber, "2"), mToken(tokenTraveler, "Adults"), tAnd,
			mToken(tokenNumber, "3"), mToken(tokenTraveler, "Children"), tIn, mToken(tokenCabin, "Economy"),
			tEOF,
		},
	},
}

func collectLexTest(t *lexTest) (tokens []token) {
	l := newQueryLexer(t.name, t.input)
	for {
		token := l.nextToken()
		tokens = append(tokens, token)
		if token.typ == tokenEOF || token.typ == tokenError {
			break
		}
	}
	return
}

func equal(i1, i2 []token, checkPos bool) bool {
	if len(i1) != len(i2) {
		return false
	}
	for k := range i1 {
		if i1[k].typ != i2[k].typ {
			return false
		}
		if i1[k].val != i2[k].val {
			return false
		}
		if checkPos && i1[k].pos != i2[k].pos {
			return false
		}
	}
	return true
}

func TestLex(t *testing.T) {
	for _, test := range lexTests {
		tokens := collectLexTest(&test)
		if !equal(tokens, test.tokens, false) {
			t.Errorf("%s: got\n\t%+v\nexpected\n\t%v", test.name, tokens, test.tokens)
		}
	}
}
