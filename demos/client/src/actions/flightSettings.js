import {SET_CABIN, ADD_PASSENGER, REMOVE_PASSENGER} from './types';

export const setCabin = id => {
  return {
    type: SET_CABIN,
    id
  }
};

export const addPassenger = id => {
  return {
    type: ADD_PASSENGER,
    id
  }
};

export const removePassenger = id => {
  return  {
    type: REMOVE_PASSENGER,
    id
  }
};