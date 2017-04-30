import {SET_TRIP} from './types';

export const setTrip = trip => {
  return {
    type: SET_TRIP,
    trip
  }
};