import {applyMiddleware, compose, createStore} from "redux";
import reducer from "./reducers/appReducer";
import initialState from "./data/initialState";
import thunk from "redux-thunk";

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const user = {
  firstName: '',
  lastName: ''
};

const store = createStore(
    reducer,
    initialState,
    user,
    composeEnhancers(applyMiddleware(thunk)));

export default store;