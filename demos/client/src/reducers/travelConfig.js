import * as types from '../actions/actionTypes';
import {Map} from 'immutable';
import {countTravelers} from '../util/travelConfig';

const travelConfig = (state = Map({}), action) => {
  switch (action.type) {
    case types.CHANGE_TRAVEL_TYPE:
      return state.set('type', action.value);

    case types.CHANGE_TRAVEL_DATE:
      if(!state.getIn(['dates', 'depart']) && !state.getIn(['dates', 'return'])) {
        state = state.setIn(['dates', 'depart'], action.date);
        state = state.setIn(['dates', 'return'], action.date);
        return state;
      }
      if(state.get('type')==='roundTrip') {
        if(action.dateType==='depart'){
          if(action.date > state.getIn(['dates','return'])) {
            state = state.setIn(['dates','return'], action.date);
          }
          state = state.setIn(['dates','depart'], action.date);
        }else if(action.dateType==='return') {
          if(action.date < state.getIn(['dates', 'return'])) {
            state = state.setIn(['dates', 'depart'], action.date);
          }
          state = state.setIn(['dates','return'], action.date);
        }
      }
      return state;
      
    case types.CHANGE_TRAVELER_TYPE_COUNT:
      let count = countTravelers(state);
      let travelercount = state.getIn(['travelers', action.typeTraveler]);
      
      if(count>=6 && action.count > travelercount) return state;
        if(count > 1){
          if(action.count>=0) state = state.setIn(['travelers',action.typeTraveler], action.count);
        }else{
          if(action.count>0) state = state.setIn(['travelers',action.typeTraveler], action.count);
        }
      return state;
      
    case types.CHANGE_CABIN:
      return state.set('cabin', action.cabin);

    case types.CHANGE_TRAVEL_FROM:
      state = state.setIn(['locations','from'], action.airport);
      return state.set('airports', []);

    case types.CHANGE_TRAVEL_TO:
      state = state.setIn(['locations','to'], action.airport);
      return state.set('airports', []);

    case types.SEARCH_AIRPORT_SUCCESS:
      return state.set('airports', action.airports);

    case types.EXCHANGE_DESTINATIONS:
      state = state.setIn(['locations','from'], action.to);
      return state.setIn(['locations','to'], action.from);

    case types.SEARCH_AVAILABLE_FLIGHTS_SUCCESS:
      return state.set('availableFlights', action.flights);

    case types.CLEAN_TRAVEL_FROM:
      return state.setIn(['locations', 'from'], {});

    case types.CLEAN_TRAVEL_TO:
      return state.setIn(['locations', 'to'], {});

    default:
      return state;
  }
};

export default travelConfig;
