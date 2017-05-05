import * as types from './actionTypes';

export const changeTravelType = type => {
  return {
    type: types.CHANGE_TRAVEL_TYPE,
    value: type
  };
};

export const changeTravelDate = (dateType, date) => {
  return {
    type: types.CHANGE_TRAVEL_DATE,
    dateType,
    date
  };
};

export const decreasePassanger = (typePassanger, count) => {
  return {
    type: types.DECREASE_PASSANGER,
    typePassanger,
    count
  };
};

export const increasePassanger = (typePassanger, count) => {
  return {
    type: types.INCREASE_PASSANGER,
    typePassanger,
    count
  };
};