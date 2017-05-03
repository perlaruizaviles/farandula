import {Map} from 'immutable';

const travelConfig = (state = Map({}), action) => {
  console.log(action);
  switch (action.type) {
    case 'CHANGE_TRAVEL_TYPE':
      return state.set('type', action.value);
    default:
      return state;
  }
};

export default travelConfig;