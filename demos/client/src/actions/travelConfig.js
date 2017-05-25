import * as types from "./actionTypes";
import airportApi from "../api/airportsApi";
import availableFlightApi from "../api/availableFlightsApi";
import {ajaxCallError, beginAjaxCall} from "./ajaxStatusActions";

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

export const cabinChange = cabin => {
  return {
    type: types.CHANGE_CABIN,
    cabin
  };
};

export const changeTravelFrom = airport => {
  return {
    type: types.CHANGE_TRAVEL_FROM,
    airport
  };
};

export const changeTravelTo = airport => {
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

export const searchAirportSuccess = airports => {
  return {
    type: types.SEARCH_AIRPORT_SUCCESS,
    airports
  };
};

export const searchAvailableFlightsSuccess = flights => {
  return {
    type: types.SEARCH_AVAILABLE_FLIGHTS_SUCCESS,
    flights
  };
};

export const cleanTravelFrom = () => {
  return {
    type: types.CLEAN_TRAVEL_FROM
  };
};

export const cleanTravelTo = () => {
  return {
    type: types.CLEAN_TRAVEL_TO
  };
};

export function searchAirport(query, quantum) {
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
}

export function searchAvailableFlights(search) {
  return (dispatch) => {
    dispatch(beginAjaxCall());
    return availableFlightApi.getAvailableFlights(search).then(flights => {
      dispatch(searchAvailableFlightsSuccess(flights));
    }).catch(error => {
      dispatch(ajaxCallError(error));
      throw(error);
    });
  };
}

export const cleanField = quantum => {
  return (dispatch) => {
    if (quantum === "from") {
      dispatch(cleanTravelFrom());
    } else if (quantum === "to") {
      dispatch(cleanTravelTo());
    }
  };
};

export const changeDepartDate = (dateA, dateB, shouldAbeBigger) => {
  if (dateA.diff(dateB) > 0){
    return [dateA, dateA]
  } else {
    return [dateA, dateB];
  }
}

export const changeReturnDate = (dateA, dateB) => {
  if (dateA.diff(dateB) > 0){
    return [dateB, dateB]
  } else {
    return [dateA, dateB];
  }
}

export const changeDate = (targetDate, otherDate, shouldTargetBeFirst = true) => {
  if (targetDate.diff(otherDate) > 0){
    return (shouldTargetBeFirst)? [targetDate, targetDate] : [otherDate, otherDate]
  } else {
    return [targetDate, otherDate];
  }
}

export const changeDates = (dates, newDate, dateType) => {
  if(dateType==='depart'){
    dates = changeDate(dates.get('depart'), newDate);
  } else if (dateType==='return'){
    changeDate(newDate, dates.get('return'));
  }
  return dates;
}