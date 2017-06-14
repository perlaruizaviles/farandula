package main

import (
	"farandula"
	"farandula/luisa"
	"log"
	"fmt"
)

func main() {
	gds, err := farandula.NewSabreGDS()
	if err != nil {
		log.Fatal(err)
	}
	luisa.SetGDS(*gds)

	query, err := farandula.Query(`
		search flights from DFW to CDG departing 2017-07-09 then returning 2017-07-11 for
    2 Adults and 3 Children in Economy
	`)
	if err != nil {
		log.Fatal(err)
	}
	result, err := luisa.FindMeFlights(*query)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println(result)
}
