import {List, Map} from "immutable";

const initialState = {
  ajaxCallsInProgress: 0,
  itineraries: Map({
    order: 'price-low-to-high',
    filters: Map({
      limit: '50',
      airlines: Map({
        'aeromexico': false,
        'volaris': false,
        'interjet': false
      })
    }),
    itinerariesList: List([])
  }),
  travelConfig: Map({
    type: 'round',
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

export default initialState;
