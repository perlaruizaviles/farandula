import * as types from './actionTypes';

export const changeTravelType = type => {
  return {
    type: types.CHANGE_TRAVEL_TYPE,
    value: type
  };
};

export const changeTravelDate = (dateType, date) => {
  return {
    type: 'CHANGE_TRAVEL_DATE',
    dateType,
    date
  };
};
