import {combineReducers} from "redux";
import {routerReducer} from "react-router-redux";
import travelConfig from "./travelConfig";
import ajaxStatusReducer from "./ajaxStatusReducer";

const reducer = combineReducers({
  travelConfig: travelConfig,
  ajaxCallsInProgress: ajaxStatusReducer,
  routing: routerReducer
});

export default reducer;
