import * as types from "../actions/actionTypes";
import {Map} from "immutable";
import {countTravelers} from "../util/travelConfig";
import {changeTravelDates} from "../util/dates";

const travelConfig = (state = Map({}), action) => {
  switch (action.type) {
    case types.CHANGE_TRAVEL_TYPE:
      return state.set('type', action.travelType);

    case types.CHANGE_TRAVEL_DATE:
      let dates = changeTravelDates(state.get('dates'), action.date, action.dateType);
      return state.set('dates', dates);

    case types.CHANGE_TRAVELER_TYPE_COUNT:
      let count = countTravelers(state);
      let travelercount = state.getIn(['travelers', action.typeTraveler]);

      if (count >= 6 && action.count > travelercount) return state;
      if (count > 1) {
        if (action.count >= 0) state = state.setIn(['travelers', action.typeTraveler], action.count);
      } else {
        if (action.count > 0) state = state.setIn(['travelers', action.typeTraveler], action.count);
      }
      return state;

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

    default:
      return state;
  }
};

export default travelConfig;
