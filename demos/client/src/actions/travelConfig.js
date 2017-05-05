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


export const searchAirportSuccess = (airports) => {
  return {
    type: types.SEARCH_AIRPORT_SUCCESS,
    airports
  };
};


export const searchAirport = (query, quantum) => {
  return (dispatch) => {
    return airportApi.searchAirport(query).then(airports => {
        const filteredAirports = airports.filter(airport => {
          return !airport.title.includes(quantum.title);
        });
        dispatch(searchAirportSuccess(filteredAirports));
    }).catch(error => {
        throw(error);
    });
  };
};

