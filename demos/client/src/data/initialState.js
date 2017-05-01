import defaultFlightSettings from './defaultFlightSettings';
import flightOptions from './flightOptions';
import moment from 'moment';

const initialState = {
  flightSettings: defaultFlightSettings,
  flightOptions: flightOptions,
  activeTripItem: 'roundTrip',
  startDate: moment(),
  endDate: moment().add(1,"day"),
  minDate: moment(),
  maxDate: moment().add(1,"year")
};

export default initialState;
