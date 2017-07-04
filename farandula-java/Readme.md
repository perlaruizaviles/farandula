# Farandula Java

Farandula is library to unify SDKs and APIs of three of the most popular GDS (Sabre, Amadeus, Travelport). 


## GDS Explanation
A Global Distribution System is a network operated by a company which main function consists in enabling automated transactions between travel service providers (mainly airlines, hotels and car rental companies) and travel agencies. 
It can link services, rates and bookings consolidating products and services across all three travel sectors: i.e., airline reservations, hotel reservations, car rentals.

Farandula uses and feeds from three of the most popular GDS in the world:

- Amadeus
- Travelport
- Sabre

Each GDS has its own implementation since requests and responses are built different.

## Luisa Assistant
Luisa is a class with an assistant role. The main purpose of this class is provide the initial information before to build a search request (flights). Luisa implements different methods to make the search components using more intuitive.

Luisa has the static method `using` which is used to specify the supplier to be used and return a Luisa instance based on that supplier.

```
//Create a GDS based connector to be used as flight supplier
SabreFlightConnector sabre = new SabreFlightConnector();

//create the Luisa instance based on that supplier
Luisa luisaAssistant = Luisa.using(sabre);
```

Once the luisa instance based on certain supplier is created, is possible to make a request using the `findMeFlights`method. This method returns a `SearchCommand` instance (explained in *models* section) ready to start building the corresponding search (explained in *SearchCommand* section).

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

- `to`: Receives a String list of arrival Airport Codes . When selecting oneWay or roundTrip the length of the list is 1, on the other hand when selected multiCity the list length goes between 2 and 5.

- `departingAt`: Receives a LocalDateTime list of departure Date.

- `returningAt`: Receives a LocalDateTime list of arrival Date. This method is only used when selected roundTrip.

- `type`: Receives and sets the FlightType (ONEWAY, ROUNDTRIP, OPENJAW). Default is set to ONEWAY.

- `preferenceClass`: Receives and sets the CabinClassType (ECONOMY, PREMIUM_ECONOMY, FIRST, BUSINESS, ECONOMYCOACH, OTHER). Default is set to ECONOMY.

- `limitTo`: Receives an integer which tells the maximum number of results expected. Default is set in 50.

- `forPassengers`: Appends a list of Passengers specifying their type (ADULTS, CHILDREN, INFANTS, INFANTSONSEAT) and how many. When not specifying any passengers a default passenger list of just one adult is set.

Finally, method `execute` returns the result already parsed as an Itinerary list. See the Models section for more information on Itinerary model.

## Models
### Airleg
The Airleg model contains the information about a complete flight. As a brief summary, it contains the information about departure and arrival airports identified by an IATA code (String), as well it contains the departure and arrival dates and times (LocalDateTime). It also contains a list of `Segment` elements to indicate the complete sections of the current flight. An airleg is identified by an id (String).

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
The CabinClassType is an enum which contains the most common cabin types to specify on the flight search commands.

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
FlightType is an enum which contains the most common flight types to perform on a search. The types are the following:
*    `ONEWAY`
*    `ROUNDTRIP`
*    `OPENJAW` (or multi-city)

The flight type is specified on the search command and is used by all GDS managers without distinction.


### Itinerary
The itinerary model is basically a result from a search request using the `FlightSearchCommand`. It contains the complete information for a flight result.

```
public class Itinerary {

    private List<AirLeg> airlegs = new ArrayList<>();
    private Fares price;
    ...
}
```

The airLegs list not only contains the Airlegs corresponding to a result, it contains exactly the number of air legs according to the flight type invoked with the search command. For example, if a `OneWay` flight is invoked, your result would be a list of n itineraries, where n < limitTo, and in which each itinerary has an airlegs list of just one element. 

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
__Note:__ number of infants (infants on lap) can never be greater than adults, since it actually makes no sense.
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

## Farandula Flow
The following image describes how Farandula works and the whole process that takes place in order to retrieve the expected flight results.

![farandula](https://user-images.githubusercontent.com/26683615/27839504-5647c46a-60b7-11e7-92ea-aba9d630a17a.png)

## Examples
For all the examples described here, it's important to specify the desired flight manager to Luisa assistant.

### One Way Flight
First, the flight connector is created to be passed to Luisa Assistant. The Travelport flight connector (GDS manager) will be used in this example.

*Java*
```
FlightConnector travelportConnector = new TravelportFlightConnector();
```

The flight search is the following: A list of itineraries is needed, it must contain results for flights departing from Dallas Forth Worth airport and arriving at Charles de Gaulle airport; the flight must depart on August 8, 2017. The passengers who will travel are two adults and a couple of children (six and eight years old respectively). The preference class is the economy, and only 20 results are required to select a flight.

All the requirements for the one way flight must be specified from the data above. The needed data and chosen values are the following:
*    `from` - DFW (IATA code for Dallas-Forth Worth airport)
*    `to` - CDG (IATA code for Charles de Gaulle airport)
*    `departing at` - 2017/08/08 00:00:00
*    `for passengers`: two adults and two children (six and eight years)
*    `preference class`: ECONOMY
*    `flight type`: ONEWAY
*    `limit to` 20 results 

The `from`, `to` and `departing at` are values which must be contained in a List of elements. The declaration of these values could be as follows.

*Java*
```
//IATA codes must be String values
List<String> fromList = new ArrayList<>();
fromList.add("DFW");

List<String> toList = new ArrayList<>();
toList.add("CDG");

//departing dates must be DateTime values
List<LocalDateTime> departingAtList = new ArrayList<>();
LocalDateTime departingDate = LocalDateTime.of(2017, 8, 8, 0, 0, 0);
departingAtList.add(departingDate);
```

The value `for passengers` must be managed by the Passenger class, and it contains different methods for adults and the rest of passenger types (children, infants, and infants on the seat). For adults, is just necessary to pass the desired number of adults to `adults` function. For the rest of passengers, the value passed to each function (`children`, `infants` and `infantsOnSeat`) must be an ages array for each passenger.

*Java*
```
int numberOfAdults = 2;
int [] childrenAges = new int[]{6, 8};
```

*    The preference class type value is provided by the enum `CabinClassTye` (default value is `ECONOMY`).
*    The flight type is provided by the `FlightType` enum (default value is `ONEWAY`).
*    The limit must be an `int` value.

*Java*
```
//CabinClassType and FlightType can be specified directly to builder
int limit = 20;
```

The request using the previous data will return a `List` of `Itinerary` elements.

The result code for that request is the following (Using Luisa assistant):

*Java*
```
List<Itinerary> itineraries = Luisa.using(travelportConnector).findMeFlights()
                                    .from(fromList)
                                    .to(toList)
                                    .departingAt(departingAtList)
                                    .forPassengers(Passenger.adults(numberOfAdults))
                                    .forPassengers(Passenger.children(childrenAges))
                                    .preferenceClass(CabinClassType.ECONOMY)
                                    .type(FlightType.ONEWAY)
                                    .limitTo(limit)
                                    .execute();
```

The following example shows how would the code look like in a default implementation (Using Luisa assistant):

*Java*
```
List<Itinerary> itineraries = Luisa.using(travelportConnector).findMeFlights()
                                    .from(fromList)
                                    .to(toList)
                                    .departingAt(departingAtList)                                    
                                    .execute();
```
In this example Luisa is searching flights from the same origin, to the same destination, and at the same departure date as the previous example. Since we are not specifying, passengers, cabin class, flight type, or limit, Luisa will automatically build the search command with their respective defaults. The request will be looking for one way flights available for one adult in economy class and retrieving a maximum number of 50 results. 

### Round Trip Flight
To specify a round trip request on the farandula library is necessary to consider some extra data (return date) and change some fields on the search command.

The flight connector to be used in this example is `Amadeus`.

*Java*
```
FlightConnector amadeusConnector = new AmadeusFlightConnector();
```

The flight search is the following: A list of itineraries is needed, it must contain results for flights departing from Dallas Forth Worth airport and arriving at Charles de Gaulle airport; the flight must depart on August 8, 2017, and return on September 8, 2017. The passengers who will travel are one adult and one infant, the infant requires a seat. The preference class is business, and only 20 results are required to select a flight.

The information to use in the request is the following:
*    `from` - DFW (IATA code for Dallas Forth Worth airport)
*    `to` - CDG (IATA code for Charles de Gaulle airport)
*    `departing at` - 2017/08/08 00:00:00
*    `returning at` - 2017/09/08 00:00:00
*    `for passengers`: one adult and one infant on seat (2 year)
*    `preference class`: BUSINESS
*    `flight type`: ROUNDTRIP
*    `limit to` 20 results 

The returning at field must be a list of elements, just as the departing at field.

*Java*
```
List<LocalDateTime> returningAtList = new ArrayList<>();
LocalDateTime returningAt = LocalDateTime.of(2017, 9, 8, 0, 0, 0);
returningAtList.add(returningAt);
```

The common fields of one-way example could be declared as the same way.

*Java*
```
//IATA codes must be String values
List<String> fromList = new ArrayList<>();
fromList.add("DFW");

List<String> toList = new ArrayList<>();
toList.add("CDG");

//departing dates must be DateTime values
List<LocalDateTime> departingAtList = new ArrayList<>();
LocalDateTime departingDate = LocalDateTime.of(2017, 8, 8, 0, 0, 0);
departingAtList.add(departingDate);

//Passengers
int numberOfAdults = 1;
int [] infantOnSeatAges = new int[]{2};

//Limit
int limit = 20;
```

The result of calling the request via Luisa assistant must be a list of `Itinerary` elements. The result request is the following:

*Java*
```
List<Itinerary> itineraries = Luisa.using(amadeustConnector).findMeFlights()
                                    .from(fromList)
                                    .to(toList)
                                    .departingAt(departingAtList)
                                    .returningAt(returningAtList)
                                    .forPassengers(Passenger.adults(numberOfAdults))
                                    .forPassengers(Passenger.infantsOnSeat(infantOnSeatAges))
                                    .preferenceClass(CabinClassType.BUSINESS)
                                    .type(FlightType.ROUNDTRIP)
                                    .limitTo(limit)
                                    .execute();
```

### Open Jaw Flight (Multi-City)
The open jaw request could be seen as the same request for a one way, but it changes the implementation by adding more elements to each IATA code list and departing dates list.

A quick view for this, for each flight, must exist an element on fromList, toList, and departingAt list.

For this example, the flight connector used is Sabre.

*Java*
```
FlightConnector sabreConnector = new SabreFlightConnector();
```

The flight search is the following: 

A list of itineraries is needed, it must contain results for flights different flights. 
*    The first flight must depart from Dallas Forth Worth airport and arrive at Charles de Gaulle airport and must depart on August 8, 2017.
*    The second flight must depart from Mexico City and arrive at Hermosillo city, it must depart on August 20, 2017. 
*    The third flight must depart from Las Vegas and arrive in Guadalajara city, it must depart on September 8, 2017. 
    
The passengers who will travel are two adults, one child (10 years old) and one infant (1-year-old) without seat. The preference class is economy, and only 20 results are required to select a flight.

The specified data condensed are the following:
*    `from`: DFW, MEX, LAS (list of departing IATA codes).
*    `to`: CDG, HMO, GDL (list of arrival IATA codes).
*    `departing at`: 2017/08/07 00:00:00, 2017/08/20 00:00:00, 2017/09/08 00:00:00 (list of departing date times)
*    `for passengers`: Two adults, one ten years old child, and one-year-old infant without seat
*    `preference class`: ECONOMY
*    `flight type`: OPENJAW
*    `limit to`: 20

The definition of all the previous data could be as follows:

*Java*
```
//From list
List<String> fromList = new ArrayList<>();
fromList.add("DFW");
fromList.add("MEX");
fromList.add("LAS");

//To list
List<String> toList = new ArrayList<>();
toList.add("CDG");
toList.add("HMO");
toList.add("GDL");

//Departing at list
List<LocalDateTime> departingAtList = new ArrayList<>();

LocalDateTime firstDate = LocalDateTime.of(2017, 8, 8, 0, 0, 0);
departingAtList.add(firstDate);

LocalDateTime secondDate = LocalDateTime.of(2017, 8, 20, 0, 0, 0);
departingAtList.add(secondDate);

LocalDateTime thirdDate = LocalDateTime.of(2017, 9, 8, 0, 0, 0);
departingAtList.add(thirdDate);

//Passengers
int numberOfAdults = 2;
int [] childrenAges = new int[]{10};
int [] infantAges = new int[]{1};

//Limit
int limit = 20;
```

The request using Luisa assistant is the following;

*Java*
```
List<Itinerary> itineraries = Luisa.using(sabreConnector).findMeFlights()
                                    .from(fromList)
                                    .to(toList)
                                    .departingAt(departingAtList)
                                    .forPassengers(Passenger.adults(numberOfAdults))
                                    .forPassengers(Passenger.infants(infantAges))
                                    .forPassengers(Passenger.children(childrenAges))
                                    .preferenceClass(CabinClassType.ECONOMY)
                                    .type(FlightType.OPENJAW)
                                    .limitTo(limit)
                                    .execute();
```
