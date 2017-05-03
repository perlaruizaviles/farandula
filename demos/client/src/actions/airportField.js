import {SET_LOADING, SET_TRAVEL_FROM, SET_TRAVEL_TO, SET_RESULTS} from './types';

const setLoading = isLoading => {
  return {
    type: SET_LOADING,
    isLoading
  }
};

const setTravelFrom = travelFrom => {
  return {
    type: SET_TRAVEL_FROM,
    travelFrom
  }
};

const setTravelTo = travelTo => {
  return {
    type: SET_TRAVEL_TO,
    travelTo
  }
};

const setResults = results => {
  return {
    type: SET_RESULTS,
    results
  }
};

export {setLoading, setTravelFrom, setTravelTo, setResults};
