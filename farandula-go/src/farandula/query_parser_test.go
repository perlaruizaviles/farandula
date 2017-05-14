package farandula

import (
	"testing"
	"time"
)

type parseTest struct {
	name  string
	input string
	query *GDSQuery
}

var july_9_2017, _ = time.Parse("2006-01-02", "2017-07-09")
var july_11_2017, _ = time.Parse("2006-01-02", "2017-07-11")
var july_15_2017, _ = time.Parse("2006-01-02", "2017-07-15")

var parseTests = []parseTest{
	{
		"one way",
		"search flights from DFW to CDG departing 2017-07-09 for 1 Adult in Economy",
		&GDSQuery{
			Itinerary: []FlightQuery{
				{From: "DFW", To: "CDG", At: july_9_2017},
			},
			Travelers: TravelerQuery{
				Adult: 1,
			},
			Cabin: Economy,
		},
	},
	{
		"round trip",
		`search flights from DFW to CDG departing 2017-07-09 then returning 2017-07-11 for
		2 Adults and 3 Children in Economy`,
		&GDSQuery{
			Itinerary: []FlightQuery{
				{From: "DFW", To: "CDG", At: july_9_2017},
				{From: "CDG", To: "DFW", At: july_11_2017},
			},
			Travelers: TravelerQuery{
				Adult: 2,
				Child: 3,
			},
			Cabin: Economy,
		},
	},
	{
		"multi city",
		`search flights from DFW to CDG departing 2017-07-09 then from CDG to LHR departing 2017-07-11
		for 3 Seniors and 1 Child in First Class`,
		&GDSQuery{
			Itinerary: []FlightQuery{
				{From: "DFW", To: "CDG", At: july_9_2017},
				{From: "CDG", To: "LHR", At: july_11_2017},
			},
			Travelers: TravelerQuery{
				Senior: 3,
				Child:  1,
			},
			Cabin: First,
		},
	},
	{
		"round multi city",
		`search flights for 3 Infants, 2 Seniors and 1 Adult in Economy Coach from DFW to CDG departing 2017-07-09 then
		from CDG to LHR departing 2017-07-11 then returning 2017-07-15`,
		&GDSQuery{
			Itinerary: []FlightQuery{
				{From: "DFW", To: "CDG", At: july_9_2017},
				{From: "CDG", To: "LHR", At: july_11_2017},
				{From: "LHR", To: "DFW", At: july_15_2017},
			},
			Travelers: TravelerQuery{
				Infant: 3,
				Senior: 2,
				Adult:  1,
			},
			Cabin: EconomyCoach,
		},
	},
}

func collectParseTest(p *parseTest) (*GDSQuery, error) {
	query := NewGDSQuery()
	lex := newQueryLexer(p.name, p.input)
	par := newQueryParser(lex, query)
	err := par.topLevel()
	if err != nil {
		return nil, err
	}
	return par.query, nil
}

func compareQueries(a, b *GDSQuery) bool {
	// Check for pointer equality
	if &a == &b {
		return true
	}
	// Check cabin
	if a.Cabin != b.Cabin {
		return false
	}
	// Check schedule
	if len(a.Itinerary) != len(b.Itinerary) {
		return false
	}
	for i, v := range a.Itinerary {
		if v.From != b.Itinerary[i].From {
			return false
		}
		if v.To != b.Itinerary[i].To {
			return false
		}
		if v.At != b.Itinerary[i].At {
			return false
		}
	}

	// Check travelers
	for i, v := range a.Travelers {
		if b.Travelers[i] != v {
			return false
		}
	}

	return true
}

func TestParse(t *testing.T) {
	for _, test := range parseTests {
		query, err := collectParseTest(&test)
		if err != nil || !compareQueries(query, test.query) {
			t.Errorf("%s: `%s` got\n\t%+v\nexpected\n\t%v\n\n%v", test.name, test.input, query, test.query, err)
		}
	}
}
