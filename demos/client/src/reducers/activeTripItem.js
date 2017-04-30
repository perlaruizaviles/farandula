import {SET_TRIP} from '../actions/types';

const activeTripItem = (state = 'roundTrip', action) => {
  switch (action.type) {
    case SET_TRIP:
      return action.trip;
    default:
      return state;
  }
};

export default activeTripItem;