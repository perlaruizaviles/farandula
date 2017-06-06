# Java back-end



**HELP WANTED**

- Documentation

    - [x] Describe the structure of the `src` directory.
    - [x] Explain Farandula local maven repository.
    - [x] Describe general nomencalture used on the project.
    - [ ] Describe how dependency injection works in the project.    
    - [ ] Describe how controllers and services interact with each other.
    - [x] Explain how Airport JSON info source is managed.
    - [ ] Describe the endpoint structure.
    - [ ] Describe how the response is built.
    - [ ] Describe Helpers purpose.
    - [ ] Describe how testing classes is done.
    - [ ] Show example for a simple request.
   
- Testing
    - [ ] Add tests for each new flight service members.
   
- Code
    - [ ] Change the airport source class to a hashmap implementation.


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
