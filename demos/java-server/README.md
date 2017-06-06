# Java back-end



**HELP WANTED**

- Documentation

    - [ ] Describe the structure of the `src` directory.
    - [ ] Explain Farandula local maven repository.
    - [ ] Describe general nomencalture for methods, properties, objects, and parameters.
    - [ ] Describe how dependency injection works in the project.    
    - [ ] Describe how controllers and services interact with each other.
    - [ ] Explain how Airport JSON info source is managed.
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
