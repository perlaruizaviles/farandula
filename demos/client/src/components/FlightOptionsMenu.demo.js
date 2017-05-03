import React from 'react';
import FlightOptionsMenu from './FlightOptionsMenu';
import defaultFlightSettings from '../data/defaultFlightSettings';
import flightOptions from '../data/flightOptions';

export default () => (
  <FlightOptionsMenu settings={defaultFlightSettings}
                     options={flightOptions}
                     onCabinClick={id => console.log(`${id} cabin selected`)}
                     onLessPassengerClick={id => console.log(`less ${id} type of passenger`)}
                     onMorePassengerClick={id => console.log(`more ${id} type of passenger`)}
  />
);