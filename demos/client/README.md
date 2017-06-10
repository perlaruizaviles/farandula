# ReactJS front-end

## Refactoring process

**HELP WANTED**

- Documentation
    - [x] Describe the structure of the `src` directory
    - [ ] Describe general code style conventions
    - [ ] Describe the routing mechanism
    - [X] Create a `README.md` file describing the purpuse of each directory inside `src`
        - [x] Actions
        - [x] Components
        - [x] Containers
        - [x] Data
        - [x] Reducers
        - [x] Util
    - [x] Describe the structure of the redux store
    - [ ] Describe how the redux store interacts with components
    - [ ] Describe how to test functions using jest
    - [ ] Describe how to test components using jest with enzyme
    - [ ] Describe usual test cases for react components
    - [ ] Provide components demonstrations with suffix `.demo.js`
    - [ ] Write component specifications
- Testing
    - [ ] Ensure each component has an associated test suite
    - [ ] Collect common testing computations as utility functions in `util`
    - [ ] Test the behaviour of passing malformed props to components (sane defaults with warnings)
- Code
    - [ ] Organize the redux store avoiding a flat state
    - [ ] Create reducers that correspond to expected application state changes
    - [ ] Create small and composable components

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
    
## The Redux Storage

Here is an overview of the redux sorage (ommiting the Immutable calls for simplicity).

The word *config* refers to the current selected values, in contrast with the word *options*
which refers to the range of valid values a particular configuration might hold.

Future additions to the storage will be `flightConfig` which holds the selected values related
to a specific flight plan (like the airline, stops, departure/arrival schedules, waiting hours, etc).

```javascript                                                                           
storage = {
  travelConfig: {
    type: 'round-trip',
    cabin: 'economy',
    travelers: {
      'adults': 1,
      'seniors': 0,
      'youth': 0,
      'child': 0,
      'seat-infant': 0,
      'lap-infant': 0
    },
    locations: {
      'from': {
        'iata': undefined,
        'city': undefined,
        'name': undefined
      },
      'to': {
        'iata': undefined,
        'city': undefined,
        'name': undefined
      }
    }
  },
  dates: {
    'depart': undefined,
    'return': undefined
  }
}
```

When `type` is `'one-way'` the `dates` omit the `'return'` property. When `type` is `'multi-city'` the `locations`
and `dates` are lists, the following structure illustrates the difference:

```javascript
storage = {
  travelConfig: {
    type: 'multi-city',
    ...,
    locations: [
      {
        'from': {...},
        'to': {...}
      },
      ...,
      {
        'from': {...},
        'to': {...}
      }
    ],
    dates: [
      { 'depart': undefined },
      ...,
      { 'depart': undefined }
    ]
  }
}
```

The range of valid `travelConfig` values is specified by `travelOptions`, whose structure
is sketched below.

```javascript
travelOptions = {
  type: ['round-trip', 'one-way', 'multi-city'],
  cabin: ['economy', 'premium-economy', 'business', 'first'],
  travelers: {
    'adults': [18, 64],
    'seniors': [65, Infinity],
    'youth': [12, 17],
    'child': [2, 11],
    'seat-infant': [0, 2],
    'lap-infant': [0, 2]
  },
  locations: {
    'ids': ['from', 'to'],
    'fields': ['iata', 'city', 'name']
  },
  dates: {
    'round-trip': ['depart', 'return'],
    'one-way': ['depart'],
    'multi-city': ['depart-n'],
  }
}
```

## Component Specifications

### `TextMenu`

Shows the available trip types specified in `travelOptions.type`.

Must receive `travelConfig.type` to highlight the current selection, and a function to
indicate that the user has selected an item.

### `DateSelection`

Shows a calendar with the ability to select a particular date.

Must receive a date with the same type as `travelConfig.dates.depart` and minimum/maximum
values for the valid dates.

### `DropListMenu`

Shows the available cabin categories specified in `travelOptions.cabin`.

Must receive `travelConfig.cabin` to show the current selection, and a function to indicate that
the user has selected an item.

### `NumberInput`

Shows a number and two buttons for increasing/decreasing the amount.

Must receive the current value, and two functions to indicate that the user has increased or decreased
the number.

### `TravelersMenu`

Shows the traveler categories associated with a `numberInput` and a description of it's age range.

Must receive `travelConfig.travelers` and a function to indicate that the user has increased or decreased
a particular traveler category amount.

### `AirportSelection`

Shows an input field with airport auto-completion, providing the information of `travelOptions.locations.fields`.

Must receive the selected airport (from something like `travelConfig.locations.from`), a list of partial matches,
and two functions to indicate that the user has updated the search query and that the user has selected a particular
airport.