import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';
import flightSettings from './flightSettings';
import flightOptions from './flightOptions';
import activeTripItem from './activeTripItem';

const reducer = combineReducers({
  flightSettings,
  flightOptions,
  activeTripItem,
  routing: routerReducer
});

export default reducer;