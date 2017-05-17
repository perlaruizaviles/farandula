import * as endpoint from "./apiEndpoints";
import axios from "axios";

class AvailableFlightsApi {

  static getAvailableFlights(search) {
    return new Promise((resolve, reject) => {
      axios({
        method:'get',
        url: endpoint.AVAILABLE_FLIGHTS_URL,
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
        const flights = response.data.content;
        resolve(Object.assign([], flights));
      })
        .catch(e => {
          reject(e);
        });
    });
  }
}

export default AvailableFlightsApi;
