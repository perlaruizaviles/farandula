import * as endpoint from './apiEndpoints';
import axios from 'axios';
import {List} from 'immutable';

class AvailableFlightsApi {

  static getAvailableFlights(search) {
    return new Promise((resolve, reject) => {
      axios({
        method:'get',
        url: endpoint.TEMP_AVAILABLE_FLIGHTS_URL, //TODO: Change to AVAILABLE_FLIGHTS_URL when backend work well again
        responseType:'json',
        params: {
          departureAirportCode: search.departureAirport,
          departingDate: search.departingDate,
          departingTime: search.departingTime,
          arrivalAirportCode: search.arrivalAirport,
          arrivalDate: search.arrivalDate,
          arrivalTime: search.arrivalTime,
          type: search.type,
          passenger: search.passenger
        }
      }).then((response) => {
        const flights = response.data;
        resolve(List(flights));
      })
        .catch(e => {
          reject(e);
        });
    });
  }
}

export default AvailableFlightsApi;
