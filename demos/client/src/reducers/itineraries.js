import * as types from "../actions/actionTypes";
import {Map} from 'immutable';

const itineraries = (state = Map({}), action) => {
  switch (action.type) {
    case types.SEARCH_AVAILABLE_FLIGHTS_SUCCESS:
      return state.set('itinerariesList', action.flights);

    case types.ORDER_PRICE_ASC:
      return state.set('itinerariesList', state.get('itinerariesList').sort((itinerary => itinerary.fares.basePrice.amount)));

    case types.ORDER_PRICE_DESC:
      return state.set('itinerariesList', state.get('itinerariesList').sortBy((itinerary => -itinerary.fares.basePrice.amount)));

    case types.CHANGE_FILTER_LIMIT:
      return state.setIn([
        'filters', 'limit'
      ], action.value);

    default:
      return state;
  }
};

export default itineraries;