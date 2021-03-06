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

const changeTravelerState = (typeTraveler, count) => {
  return {
    type: types.CHANGE_TRAVELER_TYPE_COUNT,
    typeTraveler,
    count
  };
};

export const changeTravelerCount = (travelerType, value, travelers) => {
  travelers = travelers.set(travelerType, travelers.get(travelerType) + value);
  let newTotalTravelers = countTravelers(travelers);
  let newTravelerCount = travelers.get(travelerType);
  let attentionTypes = ['lap-infant', 'adults'];

  return (dispatch) => {
    if (newTravelerCount >= 0 && newTravelerCount <= 6 && newTotalTravelers >= 1 && newTotalTravelers <= 6) {
      if (attentionTypes.includes(travelerType)) {
        let adultsCount = travelers.get('adults');
        let lapInfantCount = travelers.get('lap-infant');

        if (lapInfantCount <= adultsCount) dispatch(changeTravelerState(travelerType, newTravelerCount));
      } else {
        dispatch(changeTravelerState(travelerType, newTravelerCount));
      }
    }
  }
};

export const cabinChange = cabin => {
  return {
    type: types.CHANGE_CABIN,
    cabin
  };
};

export const addDestiny = () => {
  return {
    type: types.ADD_DESTINY
  };
};

export const removeDestiny = () => {
  return {
    type: types.REMOVE_DESTINY
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