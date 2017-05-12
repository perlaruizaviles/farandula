package farandula

import (
	"fmt"
	"strconv"
	"time"
)

type queryParser struct {
	lex               *queryLexer
	query             *GDSQuery
	storedAirportCode string
	storedFlight      FlightQuery
	storedDate        time.Time
	storedCabin       Cabin
	storedPassenger   Traveler
	storedCount       int
	storedToken       token
}

func newQueryParser(lex *queryLexer, query *GDSQuery) *queryParser {
	parser := &queryParser{
		query: query,
		lex:   lex,
	}
	parser.nextToken()
	return parser
}

func (par *queryParser) errorf(format string, args ...interface{}) error {
	// TODO: show the query character position where the error happened
	return fmt.Errorf(format, args...)
}

// queryParser must be initialized with storedToken with par.lex.nextToken()
func (par *queryParser) nextToken() token {
	it := par.storedToken
	if it.typ != tokenEOF {
		par.storedToken = par.lex.nextToken()
	}
	return it
}

func (par *queryParser) peek() token {
	return par.storedToken
}

func (par *queryParser) expectToken(typ tokenType) error {
	it := par.peek()
	if it.typ == typ {
		par.nextToken()
		return nil
	}
	return par.errorf("expecting token type %s, got ", typ, it)
}

func (par *queryParser) expectAirportCode() error {
	it := par.peek()
	if it.typ == tokenAirportCode {
		par.storedAirportCode = it.val
		par.nextToken()
		return nil
	}
	return par.errorf("expecting airport code, got %s", it)
}

func (par *queryParser) expectDate() error {
	it := par.peek()
	if it.typ == tokenDate {
		t, err := time.Parse("2006-01-02", it.val)
		if err != nil {
			t, err := time.Parse("2006/01/02", it.val)
			if err != nil {
				return par.errorf("malformed date '%s'", it.val)
			}
			par.storedDate = t
		} else {
			par.storedDate = t
		}
		par.nextToken()
		return nil
	}
	return par.errorf("expecting date, got %s", it)
}

func (par *queryParser) expectCabin() error {
	// Economy [Coach]? [Class]?
	// Premium [Economy]? [Class]?
	// First [Class]?
	// Business [Class]?
	// Other [Class]?
	it := par.peek()
	if it.typ == tokenCabin {
		switch it.val {
		case "Economy":
			par.nextToken()
			it = par.peek()
			if it.typ != tokenCabin {
				par.storedCabin = Economy
				return nil
			}
			switch it.val {
			case "Class":
				par.nextToken()
				par.storedCabin = Economy
				return nil
			case "Coach":
				par.nextToken()
				it = par.peek()
				switch {
				case it.typ != tokenCabin:
					par.storedCabin = EconomyCoach
					return nil
				case it.val == "Class":
					par.nextToken()
					par.storedCabin = EconomyCoach
					return nil
				default:
					return par.errorf("malformed cabin 'Economy Coach %s'", it.val)
				}
			default:
				return par.errorf("malformed cabin 'Economy %s'", it.val)
			}
		case "Premium":
			par.nextToken()
			it = par.peek()
			if it.typ != tokenCabin {
				par.storedCabin = PremiumEconomy
				return nil
			}
			switch it.val {
			case "Class":
				par.nextToken()
				par.storedCabin = PremiumEconomy
				return nil
			case "Economy":
				par.nextToken()
				it = par.peek()
				switch {
				case it.typ != tokenCabin:
					par.storedCabin = PremiumEconomy
					return nil
				case it.val == "Class":
					par.nextToken()
					par.storedCabin = PremiumEconomy
					return nil
				default:
					return par.errorf("malformed cabin 'Premium Economy %s'", it.val)
				}
			default:
				return par.errorf("malformed cabin 'Premium %s'", it.val)
			}
		case "First":
			par.nextToken()
			it = par.peek()
			switch {
			case it.typ != tokenCabin:
				par.storedCabin = First
				return nil
			case it.val == "Class":
				par.nextToken()
				par.storedCabin = First
				return nil
			default:
				return par.errorf("malformed cabin 'First %s'", it.val)
			}
		case "Business":
			par.nextToken()
			it = par.peek()
			switch {
			case it.typ != tokenCabin:
				par.storedCabin = Business
				return nil
			case it.val == "Class":
				par.nextToken()
				par.storedCabin = Business
				return nil
			default:
				return par.errorf("malformed cabin 'Business %s'", it.val)
			}
		case "Other":
			par.nextToken()
			it = par.peek()
			switch {
			case it.typ != tokenCabin:
				par.storedCabin = Other
				return nil
			case it.val == "Class":
				par.nextToken()
				par.storedCabin = Other
				return nil
			default:
				return par.errorf("malformed cabin 'Other %s'", it.val)
			}
		default:
			return par.errorf("unknown cabin '%s'", it.val)
		}
	}
	return par.errorf("expecting cabin, got %s", it)
}

func (par *queryParser) expectNumber() error {
	it := par.peek()
	if it.typ != tokenNumber {
		return par.errorf("expecting number, got %s", it)
	}
	par.nextToken()
	n, err := strconv.Atoi(it.val)
	if err != nil {
		return par.errorf("malformed number: %v", err)
	}
	par.storedCount = n
	return nil
}

func (par *queryParser) expectPassenger() error {
	it := par.peek()
	if it.typ != tokenTraveler {
		return par.errorf("expecting traveler, got %s", it)
	}
	par.nextToken()
	switch it.val {
	case "Adults":
		par.storedPassenger = Adult
		return nil
	case "Adult":
		par.storedPassenger = Adult
		return nil
	case "Children":
		par.storedPassenger = Child
		return nil
	case "Child":
		par.storedPassenger = Child
		return nil
	case "Infants":
		par.storedPassenger = Infant
		return nil
	case "Infant":
		par.storedPassenger = Infant
		return nil
	case "Seniors":
		par.storedPassenger = Senior
		return nil
	case "Senior":
		par.storedPassenger = Senior
		return nil
	default:
		return par.errorf("malformed traveler '%s'", it.val)
	}
}

func (par *queryParser) topLevel() error {
	err := par.expectToken(tokenSearch)
	if err != nil {
		return err
	}
	err = par.flightSearch()
	if err != nil {
		return err
	}
	err = par.expectToken(tokenEOF)
	if err != nil {
		return par.errorf("expecting end of query, got '%s'", par.peek())
	}
	return nil
}

func (par *queryParser) flightSearch() error {
	err := par.expectToken(tokenFlights)
	if err != nil {
		return err
	}
	return par.flightDescription()
}

func (par *queryParser) flightDescription() error {
	err := par.scheduleDescription()
	if err != nil {
		err = par.travelersDescription()
		if err != nil {
			return err
		}
		err = par.scheduleDescription()
		if err != nil {
			return err
		}
		return nil
	}
	err = par.travelersDescription()
	if err != nil {
		return err
	}
	return nil
}

func (par *queryParser) scheduleDescription() error {
	err := par.scheduleFlight()
	if err != nil {
		return err
	}
	par.query.Itinerary = append(par.query.Itinerary, par.storedFlight)
	par.scheduleListVarious()
	return nil
}

func (par *queryParser) scheduleFlight() error {
	q := FlightQuery{}
	err := par.expectToken(tokenFrom)
	if err != nil {
		return err
	}
	err = par.expectAirportCode()
	if err != nil {
		return err
	}
	q.From = par.storedAirportCode
	err = par.expectToken(tokenTo)
	if err != nil {
		return err
	}
	err = par.expectAirportCode()
	if err != nil {
		return err
	}
	q.To = par.storedAirportCode
	err = par.expectToken(tokenDeparting)
	if err != nil {
		return err
	}
	err = par.expectDate()
	if err != nil {
		return err
	}
	q.At = par.storedDate
	par.storedFlight = q
	return nil
}

func (par *queryParser) scheduleListVarious() error {
	err := par.expectToken(tokenThen)
	if err != nil {
		return err
	}
	err = par.expectToken(tokenReturning)
	if err != nil {
		err = par.scheduleDescription()
		if err != nil {
			return err
		}
		return nil
	}
	err = par.expectDate()
	if err != nil {
		return err
	}
	par.query.Itinerary = append(par.query.Itinerary, FlightQuery{
		From: par.query.Itinerary[len(par.query.Itinerary)-1].To,
		To:   par.query.Itinerary[0].From,
		At:   par.storedDate,
	})
	return nil
}

func (par *queryParser) travelersDescription() error {
	err := par.expectToken(tokenFor)
	if err != nil {
		return nil
	}
	err = par.travelersList()
	if err != nil {
		return err
	}
	err = par.expectToken(tokenIn)
	if err != nil {
		return err
	}
	err = par.expectCabin()
	if err != nil {
		return err
	}
	par.query.Cabin = par.storedCabin
	return nil
}

func (par *queryParser) travelersList() error {
	err := par.travelerCount()
	if err != nil {
		return err
	}
	par.query.Travelers[par.storedPassenger] += par.storedCount
	par.travelersListVarious()
	return nil
}

func (par *queryParser) travelerCount() error {
	err := par.expectNumber()
	if err != nil {
		return err
	}
	return par.expectPassenger()
}

func (par *queryParser) travelersListVarious() error {
	err := par.expectToken(tokenComma)
	if err != nil {
		err = par.expectToken(tokenAnd)
		if err != nil {
			return err
		}
		err = par.travelerCount()
		if err != nil {
			return err
		}
		par.query.Travelers[par.storedPassenger] += par.storedCount
		return nil
	}
	err = par.travelerCount()
	if err != nil {
		return err
	}
	par.query.Travelers[par.storedPassenger] += par.storedCount
	return par.travelersListVarious()
}
