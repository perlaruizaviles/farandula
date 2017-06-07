# Java back-end



**HELP WANTED**

- Documentation

    - [x] Describe the spring boot approach
    - [x] Describe the structure of the `src` directory.
    - [x] Explain Farandula local maven repository.
    - [x] Describe general nomencalture used on the project.
    - [x] Describe how dependency injection works in the project.    
    - [x] Describe how controllers and services interact with each other.
    - [x] Explain how Airport JSON info source is managed.
    - [x] Explain the mongo repository implementation
    - [ ] Describe the endpoint structure.
    - [ ] Describe how the response is built.
    - [ ] Describe Helpers purpose.
    - [ ] Describe how testing classes is done.
    - [ ] Show example for a simple request.
   
- Testing
    - [ ] Add tests for each new flight service members.
   
- Code
    - [ ] Change the airport source class to a hashmap implementation.

## Spring Boot Approach
Spring Boot is a micro framework which makes easier to create stand alone Spring based applications [https://projects.spring.io/spring-boot/]. This framework comes with different useful features to work faster.

- It comes with an embed Tomcat, Jetty or another java web server.
- Provides an optional starter POM to simplify the maven configuration.
- It configures Spring whenever is possible-
- No code generation and no xml configuration requiered.

To start a project using Spring Boot is necesary import the next dependencies in the POM file.

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.3.RELEASE</version>
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

The next step is declare the main method to start the application. For this is necessary a class annotated with __**@SpringBootApplication**__ and import the __SpringApplication__ class. 

The example of our application is the next:

```
@SpringBootApplication
public class JavaFarandulaApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaFarandulaApplication.class, args);
	}
}
```

### Spring Boot Data REST

The application described is going to implement a RESTful API; for this, is necesary to import other dependencies in the POM file.

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```

Once this dependency has been imported, is possible to annotate the controllers of the application with __**@RestController**__. This annotation assumes the response semantic and parse it as REST response (JSON format).

### Spring Boot Maven Plugin
There exists a maven plugin for Spring Boot which makes easier different tasks on the project. Is easier, for example, run the application or build the packages using this plugin.

The maven plugin usen in the POM file is the next:

```
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```

A quick example (used on next chapters) is the command to run the application using the maven plugin for springboot

`mvn spring-boot:run`

### Spring Boot Tests
There is a dependency dedicated to tests performance, is the `spring-boot-starter-test`. This dependency provides different libraries for testing like JUnit, Hamcrest and Mockito.

The dependency on the POM file must be as the following.

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```

## Source Directory Structure

The `src` directory contains the main back-end code base.

Here is an overview of the directory tree with a brief description of what each file and folder represents.

- `farandula/demos/java-server/src/`
    - `main/` Contains the main project sructure for the project and the resources for the application
        - `java/`
            - `com.farandula/` Package where are located the classes for the application
                - `Configuration/` Contains the configuration class Application, empty on this case
                - `Controller/` Contains the controllers for the application which will expose the serveices
                - `Exceptions/` Contains the special exceptions to handle in the application
                - `Helpers/` Contains the helper interfaces to use around the application
                    - `Implementations/` Contains the implementations of the interfaces on the parent folder
                - `models/` Contains the models for the application, used on the response build
                - `Repositories/` Contains the interfaces with query methods to use in MongoDB data source
                - `Response/` Contains classes with a response structure (deprecated)
                - `Service/` Contains the service classes, where the bussiness logic is performed
                - `JavaFarandulaApplication.java` Class with main method to run the application
        - `resources/` Contains the file resources and file properties for the application
    - `test/` Contains the test for the application classes
        - `java/`
            - `com.farandula/` Package where are located the test classes for the project clases
                - `Helpers/` Contains the classes with test Helpers, these classes make test over the performance operations
                - `Service/` Contains the classes with test Services, these classes make test over business logic
                - `JavaFarandulaApplicationTests.java` Class that contains general tests for the application
        - `resources/` Contains the file resources and file properties needed for run the test

## Farandula Local Maven repo

This project uses the Farandula library. For convinience purposes a local maven repository for Farandula was created in the java-server project. When deploying a proyect this step will help the server environment to find Farandula as a maven dependency. To achieve this run the following command on terminal:

    mvn deploy:deploy-file -Dfile=path/to/lib/farandula-1.0.0-SNAPSHOT.jar -DgroupId=com.example -DartifactId=farandula-example -Dpackaging=jar -Dversion=version_number -Durl=file:path/to/repo
    
Once done this the pom.xml file should look like this:

	<repositories>
		<repository>
			<id>farandula-example</id>
			<url>file:${project.basedir}/path/to/repo</url>
		</repository>
	</repositories>
    
Inside your dependencies you should have: 

		<dependency>
			<groupId>com.example</groupId>
			<artifactId>farandula-example</artifactId>
			<version>version_number</version>
		</dependency>

## Project Nomenclature

This project has a special nomenclature for the different components. Here is described the main struture for that nomenclature.

### General
Here are described the general nomenclatures for the project

- `Airport Code` It refers to the IATA airport code used to locate specific airport structures

- `Departing/Departure` This name is used to indicate different components or parameters like dates, times, airport codes
    _example_: `departingAirportCodes`, `departingDates`, `departingTimes`

- `Arrival` This name is used to indicate the airport code necesary to indicate the arrival airport  
    _example_: `arrivalAirportCodes`

- `Return` This name is used to indicate the date and time parameters necesaries for the round trip availability search
   _example_: `returnDates`, `returnTimes`

- `One Way` This name is used to make reference a trip with only one departing airport and one arrival airport with just one departing date.

- `Round Trip` This name is used to make reference a trp with the same features than the previous trip, but it must be aggregated the returning date and time

- `Multi City` This name is used to make reference a trip with multiple departing airports and arrival airports which make pair between them. For each pair of airports there are a departing date and time. There are no return dates or times on this concept.

### Models
Here are described the nomenclarute for the models contained on the project

- `Airport` It contains basic information for an airport used on the response.
	- Info contained: **name**, **city**, **country**, **iata code**

- `FLight` It contains the information corresponding to an _Air Leg_ of the farandula library.
	- Info contained: **departure airport**, **departureDate**, **arrival airport**, **arrival date** and **segment list**

- `Flight Segment` It contains the information for the segments or stops contained in one airleg. It's the corresponding model for the _Segment_ model on farandula library.
	- Info contained: **departure airport**, **departureDate**, **arrival airport**, **arrival date**, **marketing airline**, **operation airline**, **airplane data** and **cabin info**

- `FLight Itinerary` It contains the information corresponding to a complete itinerary, airlegs and pricing. It's the corresponding model to the _Itinerary_ model on farandula library.
	- Info contained: **key**, **type**, **flight list** and **itinerary fares**

- `Itinerary Fares` It contains the price information. It's the corresponding model to the _Fares_ model on farandula library.
	- Info contained: **base price**, **taxes price** and **total price**

## Dependency Injection

For this project, some annotations are used from the Spring framework to implement the dependency injection.

The corresponding annotation for that is _**@Autowired**_

Inside the project there are different interfaces with methods implemented on their corresponding classes. The interfaces are used in the code with the previous annotation on it.

Example of that implementation: 

```
public class FlightService {

    @Autowired
    FlightHelper flightHelper;
    @Autowired
    PassengerHelper passengerHelper;
    @Autowired
    DateParser dateParser;

	...
```
The class FlightService uses three different interfaces (dependencies) injected as class variables.

The interface (_FlightHelper_) is the next
```
public interface FlightHelper {
    Flight parseAirlegToFlight(AirLeg airleg);

    FlightSegment parseSegmentToFlightSegment(Segment segment);

    ItineraryFares parseFaresToItineraryFares(Fares fares);

    List<Flight> getFlightsFromItinerary(Itinerary itinerary);

    List<String> getCabinInformationFromSegment(Segment segment);

    int getLimitOfFlightsFromString(String limitString);
}
```
Those methods are implemented on the _FlightHelperImpl_ class, and it is marked with the __**@Component**__ annotation

```
@Component
public class FlightHelperImpl implements FlightHelper{

    @Autowired
    AirportRepository airportRepository;

    public Flight parseAirlegToFlight(AirLeg airLeg) {
```

We see also the injection of the _AirportRepository_ dependency injection.

## Controllers and Service
This project contains two controllers which exposes one endpoint each one. These endpoint recieve different parameters to perform the corresponding tasks.

In this case is exlained the example of the request for available flights.

The controller that realizes the requests is named _FlightAvailController_ and exposes just one endpoint. It uses the __**@Crossorigin**__ annotation, this for the cors configuration in irder to let all the requests from everywere. Also is marked with the __**@RestController**__ annotation to specify that this controller is going to perform rest responses.

```

@CrossOrigin(origins = "*")
@RestController
public class FlightAvailController {

    @Autowired
    FlightService flightService;

    @RequestMapping("/api/flights")
    public List<FlightItinerary> getAvailableFlights(@Valid SearchRequest request) {
        return flightService.getResponseFromSearch( request );
    }
}

```

The __**@RequestMapping**__ annotation indicates that every request to _/api/flights_ is going to call the _getAvailableFlights_ function.

The parameter of the function is a very useful model (_SearchRequest_) that help to validate some data. Inside this model there are the variables or parameters necesaries to make a request.

```

//Part of the SearchRequest model
public class SearchRequest {

    @NotNull
    private
    String departingAirportCodes;

    @NotNull
    private
    String departingDates;

    @NotNull
    private
    String departingTimes;

    @NotNull
    private
    String arrivalAirportCodes;
    ...

```
This parameter is used on the _flightService_ variable injected as a dependency inside the function call (_getResponseFromSearch_).

With this information contained on the _SearchRequest_, is possible perform a good request to the farandula library on the service.

```

public List<FlightItinerary> getResponseFromSearch(SearchRequest request) {

        List<FlightItinerary> response = new ArrayList<>();
        //Declaring for departing parameters
        List<String> departingAirportCodes = prepareAirportCodes(request.getDepartingAirportCodes());
        
```
In the previous code is possible to see the function _getResponseFromSearch_ with the _request_ parameter of type _SearchRequest_. After that, there is a call of a member function of _SearchRequest_ which returns the departing airport codes.

There exist some other ember function of _FlightService_ which use a _SearchRequest_ object or some of the variable values.

## Airport Information Structure
 
The file `farandula/demos/java-server/src/main/resources/resultAir.json` contains a list of airports with the format:
```
{
    "airports": [
        {
            "id": int,
            "name": string,
            "city": string,
	    "country": string,
	    "iata": string,
	    "type": string
        },
        ...
        ...
    ]
}
```
Also, when the class `AirportsSource` inits, it parses the `resultAir.json` and puts it into a HashMap to access it, the airports can be accesed trough the `iata` code with the function `AirportsSource.getAirport(String iataKey)`

## Mongo Repository
There exists a feature inside the project which perform an airport search into a MongoDB server. This is possible because of the `spring-data-mongo` dependency.

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

Working with this library is posible to map objects to MongoDB collections in a easy way.

The feature of airport search on mongodb uses an interface named `AirportRepository` which extends from the interface `MongoRepository` and is annotated with __**@Repository**__. Also, is necesary define the object type to store in a collection (named with the object type name) and the data type of the object key in the collection.

```

@Repository
public interface AirportRepository extends MongoRepository<Airport, String>{
...

```

Inside the interface, exsist different methods (without an explicit implementation) which works as operations over the repository. The normal methods defined by the parent interface performs actions like add or retreive elements, but it's also posible to declare methods that perform specific actions in the repository; these methods are called **query methods**.

```
//Find an airport by city name
    List<Airport> findByCityLike(@Param("city") String city);
```

These query methods are able to perform simple operation like the one above, which retreives a list of airports finding the cincidences in the collection by the city.

But in the application was necesary performing a more complex query. It was needed a query that retreive the first 10 airports whith councidences of one string on the iata code, city name or airport name, ignoring the case type.

This query method is the next:

```
//Find Airport by City name, Airport name or Iata code
    List<Airport> findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase
            (
                @Param("city") String city,
                @Param("name") String name,
                @Param("iata") String iata
            );
```

It's importan declare the three parameters on the query method. But is necesary find the coincidences of the same string. So, the implementation of this query method is the next on the `AirportService` class.

```
@Autowired
    AirportRepository airportRepository;

    public List<Airport> getResponseFromSearch(String pattern){
        return airportRepository.findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase
                (
                        pattern,
                        pattern,
                        pattern
                );
    }
```
The important things here is: The dependency injection of the AirportRepository interface, and the query method call with the same pattern (the string to find in the repository).

The results are returned by the function to the controller and the controller return that result as a JSON.

## The available flights response

On the `Service/` folder the `FlightService.java` class is found. This class is used by the `FlightAvailController.java` in the `Controller/` folder to return a JSON response with available flight according to an specified seach.


	@Autowired
    FlightService flightService;

    @RequestMapping("/api/flights")
    public List<FlightItinerary> getAvailableFlights(@Valid SearchRequest request) {
        return flightService.getResponseFromSearch( request );
    }
