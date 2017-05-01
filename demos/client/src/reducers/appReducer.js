import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';
import flightSettings from './flightSettings';
import flightOptions from './flightOptions';
import activeTripItem from './activeTripItem';
import {startDate,endDate,minDate,maxDate} from './dateFlights'

const reducer = combineReducers({
  flightSettings,
  flightOptions,
  activeTripItem,
  startDate,
  endDate,
  minDate,
  maxDate,
  routing: routerReducer
});

export default reducer;
