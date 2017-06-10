import {combineReducers} from "redux";
import {routerReducer} from "react-router-redux";
import travelConfig from "./travelConfig";
import itineraries from "./itineraries";
import ajaxStatusReducer from "./ajaxStatusReducer";
import {reducer as formReducer} from "redux-form";

const reducer = combineReducers({
  travelConfig: travelConfig,
  itineraries: itineraries,
  ajaxCallsInProgress: ajaxStatusReducer,
  form: formReducer,
  routing: routerReducer
});

export default reducer;
