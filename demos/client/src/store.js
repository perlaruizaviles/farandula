import {createStore} from 'redux';
import reducer from './reducers/appReducer';
import initialState from './data/initialState';

const store = createStore(reducer, initialState);

export default store;