import * as types from "../actions/actionTypes";
import {Map} from 'immutable';

const itineraries = (state = Map({}), action) => {
  switch (action.type) {
    case types.SEARCH_AVAILABLE_FLIGHTS_SUCCESS:
		state = state.set('order', 'price-low-to-high');
      return state.set('itinerariesList', action.flights);

    case types.ORDER_PRICE_ASC:
      state = state.set('itinerariesList', state.get('itinerariesList').sortBy((itinerary => itinerary.fares.basePrice.amount)));
      return state.set('order', 'price-low-to-high');

    case types.ORDER_PRICE_DESC:
      state = state.set('itinerariesList', state.get('itinerariesList').sortBy((itinerary => -itinerary.fares.basePrice.amount)));
      return state.set('order', 'price-high-to-low');

    case types.CHANGE_FILTER_LIMIT:
      return state.setIn([
        'filters', 'limit'
      ], action.limit);

    case types.CHANGE_AIRLINES_FILTER:
      return state.setIn([
        'filters', 'airlines', action.airline
      ], action.selected);

    default:
      return state;
  }
};

export default itineraries;