# Farandula

Library to unify the SDKs of the major GDS (Sabre, Amadeus, Travelport). THIS IS TEMPORARLY PRIVATE, IT WILL BE OPENSOURCE IN TWO MONTHS

## Getting Started

At the Front-End section we used React.

### Prerequisites

You’ll need to have Node >= 6 on your machine.

### Installing

1. Clone this branch or [download the zip](https://github.com/Nearsoft/farandula.git) and navigate to the folder in terminal.

```
cd .../farandula/demos/client/   
```

2. Installs a package, and any packages that it depends on.

```
npm install
```

3. Kickstart the application.

```
npm start
```

## Running the tests

At this project we used this test: 

### Snapshot testing 

It is a form to test our UI component without writing certain test cases. Here's how it works: A snapshot is a individual state of our UI, saved in a file. We have a set of snapshots for our UI components. Once we add a new UI feature, we can generate new snapshots for the updated UI components.

### Testing action creators

Basically are functions which return plain objects. When testing action creators we want to test whether the appropriate action creator was called and also whether the right action was returned.

We can test our Front-end execute the command 

```
npm test
```


