import {Map} from 'immutable';

const initialState = {
  travelConfig: Map({
    type: 'roundTrip',
    cabin: 'economy',
    travelers: Map({
      'adults': 1,
      'seniors': 0,
      'youth': 0,
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
    price: '999.99'
  })
};

export default initialState;
