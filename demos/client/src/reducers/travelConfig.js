import {Map} from 'immutable';

const travelConfig = (state = Map({}), action) => {
  switch (action.type) {
    case 'CHANGE_TRAVEL_TYPE':
      return state.set('type', action.value);
    case 'CHANGE_TRAVEL_DATE':
      return state.setIn(['dates','depart'], action.value);
    default:
      return state;
  }
};

export default travelConfig;
