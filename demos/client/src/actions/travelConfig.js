import * as types from './actionTypes';
import airportApi from '../api/airportsApi';

export const changeTravelType = type => {
  return {
    type: types.CHANGE_TRAVEL_TYPE,
    value: type
  };
};

export const changeTravelDate = (dateType, date) => {
  return {
    type: types.CHANGE_TRAVEL_DATE,
    dateType,
    date
  };
};

export const changeTravelFrom = (airport) => {
  return {
    type: types.CHANGE_TRAVEL_FROM,
    airport
  };
};

export const changeTravelTo = (airport) => {
  return {
    type: types.CHANGE_TRAVEL_TO,
    airport
  };
};

export function loadAirportsSuccess(airports) {
    return { type: types.LOAD_AIRPORTS_SUCCESS, airports };
}

export function loadAirports() {
    return function (dispatch) {
        return airportApi.getAllAirports().then(airports => {
            dispatch(loadAirportsSuccess(airports));
        }).catch(error => {
            throw(error);
        });
    };
}
