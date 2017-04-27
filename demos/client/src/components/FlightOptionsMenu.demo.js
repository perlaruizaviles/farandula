import React from 'react';
import FlightOptionsMenu from './FlightOptionsMenu';
import defaultFlightSettings from '../data/defaultFlightSettings';
import flightOptions from '../data/flightOptions';


export default () => (
  <FlightOptionsMenu settings={defaultFlightSettings}
                     options={flightOptions}
  />
);