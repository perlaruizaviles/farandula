import {SET_LOADING, SET_TRAVEL_FROM, SET_TRAVEL_TO, SET_RESULTS} from '../actions/types';

export const isLoading = (state = false, action) => {
  switch (action.type) {
    case SET_LOADING:
      return action.isLoading;
    default:
      return state;
  }
}

export const travelFrom = (state = "", action) => {
  switch (action.type) {
    case SET_TRAVEL_FROM:
      return action.travelFrom;
    default:
      return state;
  }
}

export const travelTo = (state = "", action) => {
  switch (action.type) {
    case SET_TRAVEL_TO:
      return action.travelTo;
    default:
      return state;
  }
}

export const results = (state = [], action) => {
  switch (action.type) {
    case SET_RESULTS:
      return action.results;
    default:
      return state;
  }
}
