import {Map} from 'immutable';
import {SET_CABIN, ADD_PASSENGER, REMOVE_PASSENGER} from '../actions/types';
import {countPassengers} from '../util/flightSettings';

const flightSettings = (state = Map({}), action) => {
  switch (action.type) {
    case SET_CABIN:
      return state.set('cabin', action.id);
    case ADD_PASSENGER: {
      let count = state.getIn(['passengers', action.id]);
      return state.setIn(['passengers', action.id], count + 1);
    }
    case REMOVE_PASSENGER: {
      let count = state.getIn(['passengers', action.id]);
      let total = countPassengers(state);
      if (total === 1 || count === 0) {
        return state;
      } else {
        return state.setIn(['passengers', action.id], count - 1);
      }
    }
    default:
      return state;
  }
};

export default flightSettings;