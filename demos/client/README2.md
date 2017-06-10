# README

**HELP WANTED**

- Documentation
  - [x] Describe the source achitecture
  - [x] Describe how requests are sent to back-end
  - [ ] Describe how JSON response is used
  - [x] Describe how testing is done

## The Source Directory Structure

The `src` directory contains the main font-end code base (`public` contains just a simple HTML file used by React
to inject HTML dynamically).

Here is an overview of the directory tree with a brief description of what each file and folder represents.

- `farandula/demos/client/src/`
    - `actions/`
        Contains *action types* and *action creators*
        - `__tests__/` Contains tests for *action types* and *action creators*
    - `api/` Contains files to connect with the *api*
    - `components/` Contains *presentational components* lacking state
        - `Avail/` Contains *avail components*
          - `__tests__/` Contains tests for *avail components*
        - `Booking/` Contains *booking components*
          - `__tests__/` Contains tests for *booking components*
        - `Common/` Contains *common components*
          - `__tests__/` Contains tests for *common components*
        - `demos/` Contains *demos components*
          - `__tests__/` Contains tests for *demos components*
    - `containers/` Contains *container components* which connect *presentational components* with the *store*
    - `data/` Contains concrete data like *options*, *initial configs*, common/extrema inputs, etc
    - `reducers/` Contains *reducers* that map the current *state* and an *action* to a new *state*
    - `util/` Contains utility functions
    - `App.js` Sets up the *routes* with the *store* as a react *component*
    - `index.js` Defines the entry point for react, injecting the `App` component in the page
    - `router.js` Defines the *routes* to guide the movement between page components
    - `store.js` Sets up the *store* with the combined reducers and an initial state

## Requests to back-end

Here we can look a diagram showing how requests are made and what happens when we receive the response data.

---
<p align="center"><img src="Diagram_calls.jpg" /></p>
---

The requests start in the component calling the action through the dispatcher. The action sends the request to the endpoint and receive a JSON. This JSON contains the back-end's response. Finally the action sends this JSON to the reducer where it is integrated to the current state and this updates the components that uses this data.

### Function for Airports request
You can find this function in `.../farandula/samples/client/src/api/airportsApi.js`

```
static searchAirport(query) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url: endpoint.AIRPORTS_URL,
        responseType: 'json',
        params: {
          pattern: query
        }
      }).then((response) => {
        const airports = response.data.map(e => {
          return {title: e.city + ' - ' + e.iata, description: e.name}
        }).slice(0, 5);
        resolve(Object.assign([], airports));
      })
        .catch(e => {
          reject(e);
        });
    });
  }
```
### Function for Flights request
You can find this function in `.../farandula/samples/client/src/api/availableFlightsApi.js`
```
static getAvailableFlights(search) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url: endpoint.AVAILABLE_FLIGHTS_URL,
        responseType: 'json',
        params: this.handleTravelType(search),
      }).then((response) => {
        const flights = response.data;
        resolve(List(flights));
      }).catch(e => {
        reject(e);
      });
    });
  }
```

## Usage of response in state

Here we can look at our initial state:

```
const initialState = {
  ajaxCallsInProgress: 0,
  itineraries: Map({
    order: 'price-low-to-high',
    filters: Map({
      limit: '50'
    })
  }),
  travelConfig: Map({
    type: 'roundTrip',
    cabin: 'economy',
    travelers: Map({
      'adults': 1,
      'child': 0,
      'seat-infant': 0,
      'lap-infant': 0
    }),
    locations: Map({
      from: Map({
        iata: undefined,
        city: undefined,
        name: undefined
      }),
      to: Map({
        iata: undefined,
        city: undefined,
        name: undefined
      })
    }),
    dates: Map({
      depart: undefined,
      return: undefined
    }),
    order: 'price-low-to-high',
    destinies: List([0, 1, 2])
  })
};
```
This state is modified when a request is sent to the back-end.

### Adding Flights from JSON response
When flights response is received we add "itinerariesList" map to the itineraries section in the current state containing the response data:

Before adding flights data:

```
{
  ...,
  itineraries: Map({
      order: 'price-low-to-high',
      filters: Map({
        limit: '50'
      })
    }),
  ...
}
```

After adding flights data:
```
{
  ...,
  itineraries: Map({
    order: 'price-low-to-high',
    filters: Map({
      limit: '50'
    }),
    itinerariesList: [
      {
        key: 12345,
        type: 'oneWay',
        airlegs: [...],
        fares: {...},
      },
      {...},
      ...
    ]
  }),
  ...
}

```

### Adding Airports from JSON response
When Airports response is received we add "Airports" list to the itineraries section in the current state containing the response data:

The result after adding this new state is:

```
{
...,
airports: [
  {
    title: 'Mexicali - MXL',
    description: 'General Rodolfo Se1nchez Taboada International Airport'
  },
  {
    title: 'Mexico City - MEX',
    description: 'Licenciado Benito Juarez International Airport'
  }
],
...
}

```


## Tests

At this project we used this test: 

### Snapshot testing 

It is a form to test our UI component without writing certain test cases. Here's how it works: A snapshot is a individual state of our UI, saved in a file. We have a set of snapshots for our UI components. Once we add a new UI feature, we can generate new snapshots for the updated UI components.

### Testing action creators

Basically are functions which return plain objects. When testing action creators we want to test whether the appropriate action creator was called and also whether the right action was returned.

We can test our Front-end execute the command 

```
npm test
```