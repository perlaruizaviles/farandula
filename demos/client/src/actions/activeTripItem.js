import {SET_TRIP_TYPE} from './types';

export const setTrip = tripType => {
  return {
    type: SET_TRIP_TYPE,
    tripType
  }
};
