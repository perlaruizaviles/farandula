import * as types from "./actionTypes";
import airportApi from "../api/airportsApi";
import availableFlightApi from "../api/availableFlightsApi";
import {ajaxCallError, beginAjaxCall} from "./ajaxStatusActions";
import {countTravelers} from "../util/travelConfig";

export const changeTravelType = type => {
  return {
    type: types.CHANGE_TRAVEL_TYPE,
    travelType: type
  };
};

export const changeTravelDate = (dateType, date) => {
  return {
    type: types.CHANGE_TRAVEL_DATE,
    dateType,
    date
  };
};

const travelerTypeCountChange = (typeTraveler, count) => {
  return {
    type: types.CHANGE_TRAVELER_TYPE_COUNT,
    typeTraveler,
    count
  };
};

export const changeTravelerCount = (travelerType, value, travelers) => {
	//console.log(travelerType, value, travelers)
	let totalTravelers = countTravelers(travelers);
	let newTotalTravelers = totalTravelers + value;
	console.log('Actualmente hay un total de '+totalTravelers+' travelers')
	return(dispatch) => {
		if (newTotalTravelers >= 1 && newTotalTravelers <= 6) dispatch(travelerTypeCountChange(travelerType, )) 
	}
}

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