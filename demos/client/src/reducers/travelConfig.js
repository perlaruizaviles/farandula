import * as types from "../actions/actionTypes";
import {Map} from "immutable";
import {changeTravelDates} from "../util/dates";


const travelConfig = (state = Map({}), action) => {
  switch (action.type) {
    case types.CHANGE_TRAVEL_TYPE:
      return state.set('type', action.travelType);

    case types.CHANGE_TRAVEL_DATE:
      let dates = changeTravelDates(state.get('dates'), action.date, action.dateType);
      return state.set('dates', dates);

    case types.CHANGE_TRAVELER_TYPE_COUNT:
      return state.setIn(['travelers', action.typeTraveler], action.count);

    case types.CHANGE_CABIN:
      return state.set('cabin', action.cabin);

    case types.CHANGE_TRAVEL_FROM:
      state = state.setIn(['locations', 'from'], action.airport);
      return state.set('airports', []);

    case types.CHANGE_TRAVEL_TO:
      state = state.setIn(['locations', 'to'], action.airport);
      return state.set('airports', []);

    case types.SEARCH_AIRPORT_SUCCESS:
      return state.set('airports', action.airports);

    case types.EXCHANGE_DESTINATIONS:
      state = state.setIn(['locations', 'from'], action.to);
      return state.setIn(['locations', 'to'], action.from);

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
