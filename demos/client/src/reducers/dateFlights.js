import {SET_DATE_FIRST_FLIGHT, SET_DATE_SECOND_FLIGHT} from '../actions/types';
import moment from 'moment';

export const maxDate = () => moment().add(1, "year");
export const minDate = () => moment();

export const startDate = (state = moment(), action) => {
  switch (action.type){
    case SET_DATE_FIRST_FLIGHT:
      return action.dateFirstFlight;
    default:
      return state;
  }
};

export const endDate = (state = moment().add(1, "day"), action) => {
  switch (action.type){
    case SET_DATE_SECOND_FLIGHT:
      return action.dateSecondFlight;
    default:
      return state;
  }
};
