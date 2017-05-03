import {Map} from 'immutable';
import moment from 'moment';

const initialState = {
  travelConfig: Map({
    type: 'round-trip',
    cabin: 'economy',
    minDate: moment(),
    maxDate: moment().add(1,"year"),
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
    dates: Map({
      depart: moment(),
      return: moment().add(1,"day")
    })
  })
};

export default initialState;
