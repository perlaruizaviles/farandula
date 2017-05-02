import {Map} from 'immutable';

const initialState = {
  travelConfig: Map({
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
    dates: {
      depart: undefined,
      return: undefined
    }
  })
};

export default initialState;