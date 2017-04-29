import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';
import flightSettings from './flightSettings';
import flightOptions from './flightOptions';

const reducer = combineReducers({
  flightSettings,
  flightOptions,
  routing: routerReducer
});

export default reducer;