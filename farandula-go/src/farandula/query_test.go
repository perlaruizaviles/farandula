package farandula

import (
	"testing"
)

type queryTest struct {
	name   string
	format string
	args   []string
	query  *GDSQuery
}

var queryTests = []queryTest{
	{
		"one way",
		"search flights from DFW to CDG departing 2017-07-09 for 1 Adult in Economy",
		nil,
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
		nil,
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
		nil,
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
		nil,
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
	{
		"round multi city with arguments",
		`search flights for %v %s, %v %s and 1 Adult in Economy Coach from DFW to CDG departing 2017-07-09 then
		from CDG to LHR departing 2017-07-11 then returning %s`,
		[]string{"3", "Infants", "2", "Seniors", "2017-07-15"},
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

func collectQueryTest(q *queryTest) (*GDSQuery, error) {
	args := make([]interface{}, len(q.args))
	for i, v := range q.args {
		args[i] = v
	}
	query, err := Queryf(q.format, args...)
	if err != nil {
		return nil, err
	}
	return query, nil
}

func TestQuery(t *testing.T) {
	for _, test := range queryTests {
		query, err := collectQueryTest(&test)
		if err != nil || !compareQueries(query, test.query) {
			t.Errorf("%s: got\n\t%+v\nexpected\n\t%v", test.name, query, test.query)
		}
	}
}

/*
	if you read the DEVELOPMENT document and want to quickly test that the mentioned error recovery works
	uncomment the correct code from `query_parser.go` and this test.

func TestQuery2(t *testing.T) {
	query, err := Query(`search flights for 5 Adults, 1 Child and 1 Senior in First Class from HMO to LHR
	 departing 2017-05-17 returning 2017-07-13`)
	if err != nil {
		t.Error(err)
	}
	fmt.Println(query)
}
*/
