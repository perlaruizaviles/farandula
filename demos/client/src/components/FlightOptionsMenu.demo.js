import React from 'react';
import FlightOptionsMenu from './FlightOptionsMenu';
//import defaultFlightSettings from '../data/defaultFlightSettings';
import {Map} from 'immutable';
import flightOptions from '../data/flightOptions';

const settings = Map({
  cabin: 'premium-economy',
  passengers: Map({
    'adult':       1,
    'senior':      0,
    'youth':       0,
    'child':       0,
    'seat-infant': 0,
    'lap-infant':  0
  })
});

export default () => (
  <FlightOptionsMenu settings={settings}
                     options={flightOptions}
  />
);