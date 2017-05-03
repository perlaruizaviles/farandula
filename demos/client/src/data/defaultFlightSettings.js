import {Map} from 'immutable';

const defaultFlightSettings = Map({
  cabin: 'economy',
  passengers: Map({
    'adult':       1,
    'senior':      0,
    'youth':       0,
    'child':       0,
    'seat-infant': 0,
    'lap-infant':  0
  })
});

export default defaultFlightSettings;