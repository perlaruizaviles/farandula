import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';
import flightSettings from './flightSettings';
import flightOptions from './flightOptions';
import activeTripItem from './activeTripItem';
import {startDate, endDate, minDate, maxDate} from './dateFlights'
import {isLoading, travelFrom, travelTo, results} from './airportField'

const reducer = combineReducers({
  flightSettings,
  flightOptions,
  activeTripItem,
  startDate,
  endDate,
  minDate,
  maxDate,
  isLoading,
  travelFrom,
  travelTo,
  results,
  routing: routerReducer
});

export default reducer;
