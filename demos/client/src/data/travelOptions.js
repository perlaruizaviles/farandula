import {Map, List} from 'immutable';
import moment from 'moment';

const travelOptions = Map({
  type: List(['round-trip', 'one-way', 'multi-city']),
  cabin: List(['economy', 'premium-economy', 'business', 'first']),
  minDate: moment(),
  maxDate: moment().add(1,"year"),
  travelers: Map({
    'adults': List([18, 64]),
    'seniors': List([65, Infinity]),
    'youth': List([12, 17]),
    'child': List([2, 11]),
    'seat-infant': List([0, 2]),
    'lap-infant': List([0, 2])
  }),
  locations: Map({
    ids: List(['from', 'to']),
    fields: List(['iata', 'city', 'name'])
  }),
  dates: Map({
    'round-trip': List(['depart', 'return']),
    'one-way': List(['depart']),
    'multi-city': List(['depart-n'])
  }),
  airports: []
});

export default travelOptions;