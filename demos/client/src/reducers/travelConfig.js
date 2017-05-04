import * as types from '../actions/actionTypes';
import {Map} from 'immutable';

const travelConfig = (state = Map({}), action) => {
  switch (action.type) {
    case types.CHANGE_TRAVEL_TYPE:
      return state.set('type', action.value);
    case 'CHANGE_TRAVEL_DATE':
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
    default:
      return state;
  }
};

export default travelConfig;
