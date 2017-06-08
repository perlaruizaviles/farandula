import * as types from "../actions/actionTypes";
import {Map} from "immutable";

const travelConfig = (state = Map({}), action) => {
  switch (action.type) {
    case types.CHANGE_TRAVEL_TYPE:
      return state.set('type', action.travelType);

    case types.CHANGE_TRAVELER_TYPE_COUNT:
      return state.setIn(['travelers', action.typeTraveler], action.count);

    case types.CHANGE_CABIN:
      return state.set('cabin', action.cabin);

    case types.SEARCH_AIRPORT_SUCCESS:
      return state.set('airports', action.airports);

    case types.CLEAN_TRAVEL_FROM:
      return state.setIn(['locations', 'from'], {});

    case types.CLEAN_TRAVEL_TO:
      return state.setIn(['locations', 'to'], {});

    case types.ADD_DESTINY:
      if (state.get('destinies').size === 5) {
        return state;
      }
      let destinies = state.get('destinies');
      return state.set('destinies', destinies.push(destinies.size));

    case types.REMOVE_DESTINY:
      if (state.get('destinies').size === 2) {
        return state;
      }
      return state.set('destinies', state.get('destinies').pop());
      
    default:
      return state;
  }
};

export default travelConfig;
