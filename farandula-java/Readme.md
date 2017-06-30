# Farandula Java

Farandula is library to unify SDKs and APIs of the major GDS (Sabre, Amadeus, Travelport). 

- [ ] Installation.
- [ ] Getting Started.
- [x] GDS explanation.
- [x] Luisa Assistant.
- [x] Search Command. (from, to, passengers, flightType, cabin, dates, limit).
- [x] Models.
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

A more fluent example for the Luisa assistance could be:

```
//Create a GDS based connector to be used as flight supplier
SabreFlightConnector sabre = new SabreFlightConnector();

//These lines will be explained below
List<Itinerary> results = Luisa.using(sabre)
				.findMeFlights()
                                .from()
                                ...;
```
## Search Command

The `FlightSearchCommand` is a Farandula class which purpose resides in retrieving all the required information to build a correct request for the GDSs. For this class the builder design pattern is implemented with the following methods:

- `from`: Receives a String list of departure Airport Codes. When selecting oneWay or roundTrip the length of the list is 1, on the other hand when selected multiCity the list length goes between 2 and 5.

- `to`: Receives a String list of arrival Airport Codes .

- `departingAt`: Receives a LocalDateTime list of departure Date.

- `returningAt`: Receives a LocalDateTime list of arrival Date. This method is only used when selected roundTrip.

- `type`: Receives and sets the FlightType (ONEWAY, ROUNDTRIP, OPENJAW).

- `preferenceClass`: Receives and sets the CabinClassType (ECONOMY, PREMIUM_ECONOMY, FIRST, BUSINESS, ECONOMYCOACH, OTHER).

- `limitTo`: Receives an integer which tells the max number of results expected.

- `forPassengers`: Appends a list of Passengers specifying their type (ADULTS, CHILDREN, INFANTS, INFANTSONSEAT) and how many.

Finally, method `execute` returns the result already parsed and in form of an Itinerary list. See the Models section for more information on Itinerary model.

## Models
### Airleg
The Airleg model contains the information about a complete flight. As a brief resume, it contains the information about departure and arrival airports identified by an IATA code (String), as well it contains the departure and arrival dates and times (LocalDateTime). It also contains a list of `Segment` elements to indicate the complete sections of the current flight. An airleg is identified by an id (String).

```
public class AirLeg {

    String id;
    String departureAirportCode;
    LocalDateTime departingDate;
    String arrivalAirportCode;
    LocalDateTime arrivalDate;
    List<Segment> segments;
    ...
}
```

### CabinClassType
The CabinClassType is an enum which contains the main cabin types to specify on the flight search commands.

The enum values are the following:
*    `ECONOMY`
*    `PREMIUM_ECONOMY`
*    `FIRST`
*    `BUSINESS`
*    `ECONOMYCOACH`
*    `OTHER`

The use of these cabin class types does not vary in any search type, it could be used without distinction.

### Fares
Fares model contains elements which represent the different possible prices returned by a specific GDS manager. Some elements contained in this model could be empty or null, depending on each GDS implementation. Nevertheless, farandula library returns the most information available on each request.

The main values of the Fares model are the following (`Price` type):
   - `Base Price`
   - `Taxes Price`
   - `Total Price`

Also, it may contain information like price and taxes per adult, child or infants.

### FlightSearchCommand
All necessary information to send a request and retrieve a list of available flights is contained here.

Information like airport codes, date and time information, passengers, flight type, etcetera is contained in this model and used by the GDS manager to perform the flight search.

This model contains a fluent use with different methods to specify the desired information.

* `from`
* `to`
* `departingAt`
* `returningAt`
* `forPassengers`
* `limitTo`
* `type`
* `preferenceClass`

This model also contains a method to start the available flight search; the `execute` method starts the search (using the specified GDS manager) and retrieves the corresponding results.

**Note**: The use and explanation of this model has been described in the previous section

### FlighType
FlightType is an enum which contains the main flight types to perform on a search. The types are the following:
*    `ONEWAY`
*    `ROUNDTRIP`
*    `OPENJAW` (or multi-city)

The flight type is specified on the search command and is used by all GDS managers without distinction.

### GuestType
The GestType is an enum which contains the guest types managed by the library. The types are the following:
*    `ADULTS`
*    `CHILDREN`

The guest type is specified on the search command and is used by the GDS managers without distinction.

### HotelSearchCommand

This model is used to perform the hotel search. All the information necessary to make a request with a specific GDS is contained here. Information like the check-in, checkout, guest list and rooms quantity could be filled used some fluent methods.
*    `checkIn`
*    `checkOut`
*    `forGuest`
*    `rooms`

As the `FlightSeachCommand` model, the `HotelSearchCommand` also contains the `execute` method which starts the hotel search and retrieves the hotel results.

### Itinerary
The itinerary model is basically a result from a search request using the `FlightSearchCommand`. It contains the complete information for a flight result.

```
public class Itinerary {

    private List<AirLeg> airlegs = new ArrayList<>();
    private Fares price;
    ...
}
```

The airLegs list not only contains the Airlegs corresponding to a result, it contains exactly the number of air legs according to the flight type invoked with the search command. For example, if a `OneWay` flight is invoked, the airLegs must contain just one element.

This element contains a `Fare` element called price with the general itinerary prices information on it.

### Passenger
Passenger model contains the basic information to identify a passenger. It contains only the passenger type (`PassengerType`) and age.

```
public class Passenger {

    private PassengerType type;
    private int age;
    ...
}
```

This model contains also some method to perform the passenger creation:
*    `infants` - Creates infant passenger list based on age (int) array.
*    `infantsOnSeat` - Creates infant on seat passenger list based on age (int) array.
*    `children` - Creates children passenger list based on age (int) array
*    `adults` - Creates adult passenger list based on the number of adults desired (int).

Example:

```
int [] infantAges = new int[]{1,2};
List<Passenger> infantList = Passenger.intants(infantAges);
```

The result list could be used as information for the search command.

### PassengerType
PassengerType is an enum which holds the passenger type used by the search command. The types are the following:
*    `ADULTS`
*    `CHILDREN`
*    `INFANTS`
*    `INFANTSONSEAT`

These passenger types can be used without distinction with the different GDS manager.

### Price
Price model contains only two members: A string with the currency code and a double which represents the price amount.

```
public class Price {

    private double amount;
    private String currencyCode;
    ...
}
```

The Price model is used inside the `Fares` model.

### Seat
Seat Model contains the cabin class type (`CabinClassType` enum) and the place code.

```
public class Seat {

    private CabinClassType classCabin;
    private String place;
    ...
}
```

Seat model is used inside the `Segment` model.

### Segment
Segment model is one of the main components for flight results. The segment represents one 'stop' for the current airleg result. The segment list contained in the AirLeg model represents all the stops necessaries to reach the airport destination.

The information contained in the segment is the following:

*    `Operating Airline` information (name, code and flight number)
*    `Marketing Airline` information (name, code and flight number)
*    `Departure` information (airport code, date, and terminal)
*    `Arrival` information (airport code, date, and terminal)
*    `Airplane Data`
*    `Duration` in minutes
*    `Seat list` available

__Note 1.__ Sometimes the airline which offers the flight does not match with the airline who performs the flight, that's the reason of `Operating Airline` and `Marketing Airline`.

__Note 2.__ There could be some extra information depending on the GDS manager. An example is the `Fares` included only in results from the `Travelport` manager.
