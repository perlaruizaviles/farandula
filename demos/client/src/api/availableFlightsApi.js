import * as endpoint from './apiEndpoints';
import axios from 'axios';

class AvailableFlightsApi {

  static getAvailableFlights(departureAirport, departingDate, departingTime, arrivalAirport, arrivalDate, arrivalTime, type, passenger){
    return new Promise((resolve, reject) => {
      axios({
        method:'get',
        url: endpoint.AVAILABLE_FLIGHTS_URL,
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
        const flights = response.data.content;
        console.log(flights);
        resolve(Object.assign([], flights));
      })
        .catch(e => {
          reject(e);
        });
    });
  }
  static getFakeAvailableFlights(){
    return new Promise((resolve, reject) => {
      axios({
        method:'get',
        url: 'http://iotar.azurewebsites.net/api/farandula',
        responseType:'json'
      }).then((response) => {
        const flights = response.data.content;
        console.log(flights);
        resolve(Object.assign([], flights));
      })
        .catch(e => {
          reject(e);
        });
    });
  }
}

export default AvailableFlightsApi;

// EXAMPLE:

// departureAirportCode: "DFW",
//  departingDate: "2017-05-10",
//  departingTime: "10:15:30",
//  arrivalAirportCode: "CDG",
//  arrivalDate: "2017-05-16",
//  arrivalTime: "00:00:00",
//  type: "round",
//  passenger: {
//    children:2,
//    adults:2
//  }
