import defaultFlightSettings from './defaultFlightSettings';
import flightOptions from './flightOptions';

const initialState = {
  flightSettings: defaultFlightSettings,
  flightOptions: flightOptions,
  activeTripItem: 'roundTrip'
};

export default initialState;