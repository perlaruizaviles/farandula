import * as types from "../actions/actionTypes";
import {availableFlights as avails} from "../data/initialState";

const initialState = avails;

const availableFlights = (state = initialState, action) => {
  switch (action.type) {
    case types.ORDER_PRICE_ASC:
      console.log(state)
      debugger
      return state.set('availableFlights',state.get('availableFlights').sortBy((item => item.fares.totalPrice.amount)));

    case types.ORDER_PRICE_DESC:
      console.log(state)
      return state.set('availableFlights',state.get('availableFlights').sortBy((item => -item.fares.totalPrice.amount)));

    case types.CHANGE_PRICE_ORDER:
      console.log(state)
      return state.set('order', action.value);

    default:
        return state;
    }
  };

export default availableFlights;