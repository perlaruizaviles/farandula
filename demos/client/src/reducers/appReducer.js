import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';

/*
    import specific component reducers
 */

const reducer = combineReducers({
  routing: routerReducer
});

export default reducer;