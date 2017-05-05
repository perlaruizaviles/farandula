import * as types from '../actions/actionTypes';
import {Map} from 'immutable';

const travelConfig = (state = Map({}), action) => {
  switch (action.type) {
    case types.CHANGE_TRAVEL_TYPE:
      return state.set('type', action.value);

    case types.CHANGE_TRAVEL_DATE:
      if(state.get('type')==='round-trip'){
        if(action.dateType==='depart'){
          if(action.date > state.getIn(['dates','return'])){
            state = state.setIn(['dates','return'], action.date);
          }
          state = state.setIn(['dates','depart'], action.date);
        }else if(action.dateType==='return'){
          if(action.date < state.getIn(['dates', 'return'])){
            state = state.setIn(['dates', 'depart'], action.date);
          }
          state = state.setIn(['dates','return'], action.date);
        }
      }
      return state;

    case types.CHANGE_TRAVEL_FROM:
      return state.setIn(['locations','from'], action.airport);
    case types.CHANGE_TRAVEL_TO:
      return state.setIn(['locations','to'], action.airport);
    case types.SEARCH_AIRPORT_SUCCESS:
      return state.set('airports', action.airports);
    default:
      return state;
  }
};

export default travelConfig;
