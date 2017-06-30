# Farandula Java

Farandula is library to unify SDKs and APIs of the major GDS (Sabre, Amadeus, Travelport). 

- [ ] Installation.
- [ ] Getting Started.
- [x] GDS explanation.
- [x] Luisa Assistant.
- [ ] Search Command. (from, to, passengers, flightType, cabin, dates, limit).
- [ ] Models.
- [ ] Flow.
- [ ] Examples.


## GDS Explanation
A Global Distribution System is a network operated by a company which main function consists in enabling automated transactions between travel service providers (mainly airlines, hotels and car rental companies) and travel agencies. 
It can link services, rates and bookings consolidating products and services across all three travel sectors: i.e., airline reservations, hotel reservations, car rentals.

Farandula uses and feeds from three of the main GDS in the world:

- Amadeus
- Travelport
- Sabre

Each GDS has its own implementation since requests and responses are built different.

## Luisa Assistant
Luisa is a class with an assistant role. The main purpose of this class is provide the initial information before to build a search request (for hotels or flights). Luisa implements different methods to make the search components using more intuitive.

Luisa has the static method `using` which is used to specify the supplier to be used and return a Luisa instance based on that supplier, this supplier can be for hotel search or flight search.

```
//Create a GDS based connector to be used as flight supplier
SabreFlightConnector sabre = new SabreFlightConnector();

//create the Luisa instance based on that supplier
Luisa luisaAssistant = Luisa.using(sabre);
```

Once the luisa instance based on certain supplier is created, is possible to make a request using the `findMe[Fligts/Hotels]`method. This method returns a `SearchCommand` instance (explained in *models* section) ready to start building the corresponding search (explained in *SearchCommand* section).

```
SearchCommand command = luisaAssistant.findMeFlights();
```
Example above is using a command variable to hold the search command object returned by the `findMeFlights` method used by the *Luisa* assistant.

A more fluid example for the Luisa assistance could be:

```
//Create a GDS based connector to be used as flight supplier
SabreFlightConnector sabre = new SabreFlightConnector();

//These lines will be explained below
List<Itinerary> results = Luisa.using(sabre)
								.findMeFlights()
                                .from()
                                ...;
```
