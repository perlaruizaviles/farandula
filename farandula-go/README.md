# Farandula & Go

## Installation Instructions

TODO: Does `go get` work with a directory inside repo?

## How To Use

### Flight Availability

TODO: `FindMeFlights` not yet implemented

```go
func main() {
    err := luisa.SetGDS(farandula.SabreGDS)
    if err != nil {
        log.Fatalf("setting Sabre GDS: %v", err)
    }
    itineraries, err := luisa.FindMeFlights(farandula.Query(`
        search flights from DFW to CDG departing 2017-07-09 then
        returning 2017-07-11 for 2 Adults and 3 Children in Economy
    `)
    if err != nil {
        log.Fatalf("searching flight itineraries: %v", err)
    }
    // Do your own thing with the itineraries
}
```