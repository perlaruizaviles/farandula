import {combineReducers} from "redux";
import {routerReducer} from "react-router-redux";
import travelConfig from "./travelConfig";
import ajaxStatusReducer from "./ajaxStatusReducer";
import {reducer as formReducer} from "redux-form";

const reducer = combineReducers({
  travelConfig: travelConfig,
  ajaxCallsInProgress: ajaxStatusReducer,
  form: formReducer,
  routing: routerReducer
});

export default reducer;
