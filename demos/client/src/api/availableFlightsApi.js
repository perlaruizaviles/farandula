import * as endpoint from './apiEndpoints';
import axios from 'axios';
import {List} from 'immutable';

class AvailableFlightsApi {

  static getAvailableFlights(departureAirport, departingDate, departingTime, arrivalAirport, arrivalDate, arrivalTime, type, passenger){
    return new Promise((resolve, reject) => {
      axios({
        method:'get',
        url: endpoint.FAKE_URL,
        responseType:'json',
        params: {
          departureAirportCode: departureAirport,
          departingDate: departingDate,
          departingTime: departingTime,
          arrivalAirportCode: arrivalAirport,
          arrivalDate: arrivalDate,
          arrivalTime: arrivalTime,
          type: type,
          passenger: passenger
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
