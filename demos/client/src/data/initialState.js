import {Map} from "immutable";

const initialState = {
  ajaxCallsInProgress: 0,
  travelConfig: Map({
    type: 'multiCity',
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
    order: 'price-low-to-high'
  })
};

export default initialState;
