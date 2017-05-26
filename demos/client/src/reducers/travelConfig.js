import * as types from "../actions/actionTypes";
import {Map} from "immutable";
import {countTravelers} from "../util/travelConfig";
import {changeTravelDates} from "../util/dates";

const travelConfig = (state = Map({}), action) => {
  switch (action.type) {
    case types.CHANGE_TRAVEL_TYPE:
      return state.set('type', action.value);

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

    case types.SEARCH_AVAILABLE_FLIGHTS_SUCCESS:
      return state.set('availableFlights', action.flights);

    case types.CLEAN_TRAVEL_FROM:
      return state.setIn(['locations', 'from'], {});

    case types.CLEAN_TRAVEL_TO:
      return state.setIn(['locations', 'to'], {});

    case types.ORDER_PRICE_ASC:
      return state.set('availableFlights',state.get('availableFlights').sort((item => item.fares.basePrice.amount)));

    case types.ORDER_PRICE_DESC:
      return state.set('availableFlights',state.get('availableFlights').sortBy((item => -item.fares.basePrice.amount)));

    case types.CHANGE_PRICE_ORDER:
      state = state.set('order', action.order);
      if (action.order==='price-high-to-low'){
        state = state.set('availableFlights',state.get('availableFlights').sortBy((item => -item.fares.basePrice.amount)));
      } else {
        state = state.set('availableFlights',state.get('availableFlights').sortBy((item => item.fares.basePrice.amount)));
      }
      return state;
    
    case types.CHANGE_FILTER_LIMIT:
      return state.setIn(['filters', 'limit'], action.value);

    default:
      return state;
  }
};

export default travelConfig;
