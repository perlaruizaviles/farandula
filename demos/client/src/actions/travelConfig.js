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

export const travelerTypeCountChange = (typeTraveler, count) => {
  return {
    type: types.CHANGE_TRAVELER_TYPE_COUNT,
    typeTraveler,
    count
  };
};

export const cabinChange = (cabin) => {
  return {
    type: types.CHANGE_CABIN,
    cabin
  };
};
