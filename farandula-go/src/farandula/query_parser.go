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
	storedTraveler    Traveler
	storedNumber      int
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
	return par.errorf("expecting `%s`, got `%s`", typ, it.typ)
}

func (par *queryParser) expectAirportCode() error {
	it := par.peek()
	if it.typ == tokenAirportCode {
		par.storedAirportCode = it.val
		par.nextToken()
		return nil
	}
	return par.errorf("expecting airport code, got `%s`", it.typ)
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
	return par.errorf("expecting date, got `%s`", it.typ)
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
					return par.errorf("malformed cabin `Economy Coach %s`", it.val)
				}
			default:
				return par.errorf("malformed cabin `Economy %s`", it.val)
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
					return par.errorf("malformed cabin `Premium Economy %s`", it.val)
				}
			default:
				return par.errorf("malformed cabin `Premium %s`", it.val)
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
				return par.errorf("malformed cabin `First %s`", it.val)
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
				return par.errorf("malformed cabin `Business %s`", it.val)
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
				return par.errorf("malformed cabin `Other %s`", it.val)
			}
		default:
			return par.errorf("unknown cabin `%s`", it.val)
		}
	}
	return par.errorf("expecting cabin, got `%s`", it.typ)
}

func (par *queryParser) expectNumber() error {
	it := par.peek()
	if it.typ != tokenNumber {
		return par.errorf("expecting number, got `%s`", it.typ)
	}
	par.nextToken()
	n, err := strconv.Atoi(it.val)
	if err != nil {
		return par.errorf("malformed number: `%v`", err)
	}
	par.storedNumber = n
	return nil
}

func (par *queryParser) expectTraveler(n int) error {
	it := par.peek()
	if it.typ != tokenTraveler {
		return par.errorf("expecting traveler, got `%s`", it.typ)
	}
	par.nextToken()
	switch it.val {
	case "Adults":
		switch {
		case n == 0:
			return par.errorf("expecting more than one Adults")
		case n == 1:
			return par.errorf("did you mean `Adult` instead of `Adults`?")
		default:
			par.storedTraveler = Adult
			return nil
		}
	case "Adult":
		switch {
		case n == 0:
			return par.errorf("expecting one Adult")
		case n > 1:
			return par.errorf("did you mean `Adults` instead of `Adult`?")
		default:
			par.storedTraveler = Adult
			return nil
		}
	case "Children":
		switch {
		case n == 0:
			return par.errorf("expecting more than one Children")
		case n == 1:
			return par.errorf("did you mean `Child` instead of `Children`")
		default:
			par.storedTraveler = Child
			return nil
		}
	case "Child":
		switch {
		case n == 0:
			return par.errorf("expecting one Child")
		case n > 1:
			return par.errorf("did you mean `Children` instead of `Child`?")
		default:
			par.storedTraveler = Child
			return nil
		}
	case "Infants":
		switch {
		case n == 0:
			return par.errorf("expecting more than one Infants")
		case n == 1:
			return par.errorf("did you mean `Infant` instead of `Infants`?")
		default:
			par.storedTraveler = Infant
			return nil
		}
	case "Infant":
		switch {
		case n == 0:
			return par.errorf("expecting one Infant")
		case n > 1:
			return par.errorf("did you mean `Infants` instead of `Infant`?")
		default:
			par.storedTraveler = Infant
			return nil
		}
	case "Seniors":
		switch {
		case n == 0:
			return par.errorf("expecting more than one Seniors")
		case n == 1:
			return par.errorf("did you mean `Senior` instead of `Seniors`?")
		default:
			par.storedTraveler = Senior
			return nil
		}
	case "Senior":
		switch {
		case n == 0:
			return par.errorf("expecting one Senior")
		case n > 1:
			return par.errorf("did you mean `Seniors` instead of `Senior`?")
		default:
			par.storedTraveler = Senior
			return nil
		}
	default:
		return par.errorf("malformed traveler `%s`", it.val)
	}
}

func (par *queryParser) topLevel() error {
	err := par.searchTopLevel()
	if err != nil {
		return err
	}
	err = par.expectToken(tokenEOF)
	if err != nil {
		return par.errorf("too big of a query, %v", err)
	}
	return nil
}

func (par *queryParser) searchTopLevel() error {
	err := par.expectToken(tokenSearch)
	if err != nil {
		return par.errorf("not a search query, %v", err)
	}
	err = par.flightsTopLevel()
	if err != nil {
		return err
	}
	return nil
}

func (par *queryParser) flightsTopLevel() error {
	err := par.expectToken(tokenFlights)
	if err != nil {
		return par.errorf("not a flight search query, %v", err)
	}
	err = par.flightsDescription()
	if err != nil {
		return err
	}
	return nil
}

func (par *queryParser) flightsDescription() (err error) {
	if err = par.expectToken(tokenFrom); err == nil {
		err = par.scheduleDescription()
		if err != nil {
			return err
		}
		err = par.expectToken(tokenFor)
		if err != nil {
			return err
		}
		err = par.travelersDescription()
		if err != nil {
			return err
		}
	} else if err = par.expectToken(tokenFor); err == nil {
		err = par.travelersDescription()
		if err != nil {
			return err
		}
		err = par.expectToken(tokenFrom)
		if err != nil {
			return err
		}
		err = par.scheduleDescription()
		if err != nil {
			return err
		}
	} else {
		return par.errorf("expecting either `from` or `for`, got `%s`", par.storedToken.typ)
	}
	return nil
}

func (par *queryParser) scheduleDescription() (err error) {
	err = par.flightDescription()
	if err != nil {
		return err
	}
	err = par.scheduleRest()
	if err != nil {
		return err
	}
	return nil
}

func (par *queryParser) flightDescription() (err error) {
	flight := FlightQuery{}

	err = par.expectAirportCode()
	if err != nil {
		return err
	}
	flight.From = par.storedAirportCode

	err = par.expectToken(tokenTo)
	if err != nil {
		return err
	}
	err = par.expectAirportCode()
	if err != nil {
		return err
	}
	flight.To = par.storedAirportCode

	err = par.expectToken(tokenDeparting)
	if err != nil {
		return err
	}
	err = par.expectDate()
	if err != nil {
		return err
	}
	flight.At = par.storedDate

	par.query.Itinerary = append(par.query.Itinerary, flight)

	return nil
}

func (par *queryParser) scheduleRest() (err error) {
	err = par.expectToken(tokenThen)
	if err != nil {
		/*
			--------------------------------------------------------------------------------------------------------------------
				# EXAMPLE OF ERROR RECOVERY:

				Suppose the user writes the following query

				`search flights from DFW to CDG departing 2017-01-01 returning 2017 for 1 Adult`

				The query doesn't satisfy the grammar, we can infer that the user wants a round trip but forgot to write `then`
				before the `returning` keyword. Well in this `scheduleRest` method we expect to see `then` as the next token,
				and in this precise block we say that no then was found but that the schedule description is complete.

				Let's change that by checking if the next token is `returning`, in which case we proceed with the `scheduleContinue`
				method:
		*/

		/*
			if par.storedToken.typ != tokenReturning {
				return nil // next token isn't `returning` so we do the right thing with the [epsilon]
			}
			err = par.scheduleContinue()
			if err != nil { // if we guessed wrong the user's intentions
				return err // return an error, the [epsilon] production wasn't intended by the user query
			}
			return nil
		*/
		/*
			--------------------------------------------------------------------------------------------------------------------
		*/

		return nil // next token isn't `then`, so an empty match is propagated (see [epsilon] in grammar)
	}
	err = par.scheduleContinue()
	if err != nil {
		return err
	}
	return nil
}

func (par *queryParser) scheduleContinue() (err error) {
	if err = par.expectToken(tokenFrom); err == nil {
		err = par.scheduleDescription()
		if err != nil {
			return err
		}
	} else if err = par.expectToken(tokenReturning); err == nil {
		flight := FlightQuery{}
		err = par.expectDate()
		if err != nil {
			return err
		}
		flight.At = par.storedDate
		flight.From = par.query.Itinerary[len(par.query.Itinerary)-1].To
		flight.To = par.query.Itinerary[0].From

		par.query.Itinerary = append(par.query.Itinerary, flight)
	} else {
		return par.errorf("expecting either `from` or `returning`, got `%s`", par.storedToken.typ)
	}
	return nil
}

func (par *queryParser) travelersDescription() (err error) {
	err = par.travelerDescription()
	if err != nil {
		return err
	}
	err = par.travelersRest()
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

func (par *queryParser) travelerDescription() (err error) {
	err = par.expectNumber()
	if err != nil {
		return err
	}
	n := par.storedNumber
	err = par.expectTraveler(n)
	if err != nil {
		return err
	}
	par.query.Travelers[par.storedTraveler] += n
	return nil
}

func (par *queryParser) travelersRest() (err error) {
	if err = par.expectToken(tokenComma); err == nil {
		err = par.travelerDescription()
		if err != nil {
			return err
		}
		err = par.travelersForceRest()
		if err != nil {
			return err
		}
	} else if err = par.expectToken(tokenAnd); err == nil {
		err = par.travelerDescription()
		if err != nil {
			return err
		}
	} else {
		return nil // next token isn't `,` or `and`, so an empty match is propagated (see [epsilon] in grammar)
	}
	return nil
}

func (par *queryParser) travelersForceRest() (err error) {
	if err = par.expectToken(tokenComma); err == nil {
		err = par.travelerDescription()
		if err != nil {
			return err
		}
		err = par.travelersForceRest()
		if err != nil {
			return err
		}
	} else if err = par.expectToken(tokenAnd); err == nil {
		err = par.travelerDescription()
		if err != nil {
			return err
		}
	} else {
		return par.errorf("expecting either `,` or `and`, got `%s`", par.storedToken.typ)
	}
	return nil
}
