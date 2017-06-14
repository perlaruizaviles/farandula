package farandula

import (
	"fmt"
	"testing"
)

func TestNewSabreGDS(t *testing.T) {
	sabre, err := NewSabreGDS()
	if err != nil {
		t.Error(err)
	}
	fmt.Println(sabre)
}

func TestSabreGDS_GetAvail(t *testing.T) {
	query := &GDSQuery{
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
		Cabin: Economy,
	}
	sabre, err := NewSabreGDS()
	if err != nil {
		t.Error(err)
	}
	result, err := sabre.GetAvail(*query)
	if err != nil {
		t.Error(err)
	}
	fmt.Println(result)
}
