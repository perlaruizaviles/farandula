import {SET_DATE_FIRST_FLIGHT, SET_DATE_SECOND_FLIGHT} from './types';

export const setDateFirstFlight = dateFirstFlight => {
  return {
    type: SET_DATE_FIRST_FLIGHT,
    dateFirstFlight
  }
};

export const setDateSecondFlight = dateSecondFlight => {
  return {
    type: SET_DATE_SECOND_FLIGHT,
    dateSecondFlight
  }
}
