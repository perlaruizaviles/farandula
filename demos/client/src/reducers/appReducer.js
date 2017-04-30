import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';
import flightSettings from './flightSettings';
import flightOptions from './flightOptions';
import activeTripItem from './activeTripItem';
import dateFlights from './dateFlights'

const reducer = combineReducers({
  flightSettings,
  flightOptions,
  activeTripItem,
  dateFlights,
  routing: routerReducer
});

export default reducer;
