import {SET_TRIP_TYPE} from '../actions/types';

const activeTripItem = (state = 'roundTrip', action) => {
  switch (action.type) {
    case SET_TRIP_TYPE:
      return action.tripType;
    default:
      return state;
  }
};

export default activeTripItem;
