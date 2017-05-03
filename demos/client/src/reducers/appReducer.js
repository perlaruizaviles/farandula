import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';
import travelConfig from './travelConfig';

const reducer = combineReducers({
  // sub-state: reducer
  travelConfig: travelConfig,
  routing: routerReducer
});

export default reducer;
