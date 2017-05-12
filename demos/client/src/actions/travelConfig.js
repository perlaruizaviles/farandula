import * as types from './actionTypes';
import airportApi from '../api/airportsApi';
import availableFlightApi from '../api/availableFlightsApi';

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

export const travelerTypeCountChange = (typeTraveler, count) => {
  return {
    type: types.CHANGE_TRAVELER_TYPE_COUNT,
    typeTraveler,
    count
  };
};

export const cabinChange = (cabin) => {
  return {
    type: types.CHANGE_CABIN,
    cabin
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

export const exchangeDestinations = (from, to) => {
  return {
    type: types.EXCHANGE_DESTINATIONS,
    from,
    to
  };
};

export const searchAirportSuccess = (airports) => {
  return {
    type: types.SEARCH_AIRPORT_SUCCESS,
    airports
  };
};

export const searchAvailableFlightsSuccess = (flights) => {
  return {
    type: types.SEARCH_AVAILABLE_FLIGHTS_SUCCESS,
    flights
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

export const searchAvailableFlights = (departureAirport, departingDate, departingTime, arrivalAirport, arrivalDate, arrivalTime, type, passenger) => {
  console.log(passenger);
  return (dispatch) => {
    return availableFlightApi.getAvailableFlights(departureAirport,departingDate,departingTime,arrivalAirport,arrivalDate,arrivalTime,type,passenger).then(flights => {
      dispatch(searchAvailableFlightsSuccess(flights));
    }).catch(error => {
      throw(error);
    });
  };
};
