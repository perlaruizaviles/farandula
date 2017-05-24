import {combineReducers} from "redux";
import {routerReducer} from "react-router-redux";
import travelConfig from "./travelConfig";
import ajaxStatusReducer from "./ajaxStatusReducer";
import resultOptions from "./resultOptions";
import {reducer as formReducer} from "redux-form";

const reducer = combineReducers({
  travelConfig: travelConfig,
  ajaxCallsInProgress: ajaxStatusReducer,
  resultOptions: resultOptions,
  form: formReducer,
  routing: routerReducer
});

export default reducer;
