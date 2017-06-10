import {CHANGE_AIRLINES_FILTER, CHANGE_FILTER_LIMIT} from "./actionTypes";

export const changeAirlinesFilter = (airline, selected) => {
  return {
    type: CHANGE_AIRLINES_FILTER,
    airline,
    selected
  }
};

export const changeFilterLimit = limit => {
  return {
    type: CHANGE_FILTER_LIMIT,
    limit
  }
};