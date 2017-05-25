import * as endpoint from "./apiEndpoints";
import axios from "axios";
import {List} from "immutable";

class AvailableFlightsApi {

  static getAvailableFlights(search) {

    const passenger = `children:${search.passenger.get('child')},infants:${search.passenger.get('lap-infant')},infantsOnSeat:${search.passenger.get('seat-infant')},adults:${search.passenger.get('adults')}`;

    let params = {
      departureAirportCode: search.departureAirport,
      departingDate: search.departingDate,
      departingTime: search.departingTime,
      arrivalAirportCode: search.arrivalAirport,
      type: search.type,
      passenger: passenger,
      cabin: search.cabin
    };

    if (params.type === "round"){
      params.arrivalDate = search.arrivalDate;
      params.arrivalTime = search.arrivalTime;
    }

    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url: endpoint.AVAILABLE_FLIGHTS_URL,
        responseType: 'json',
        params: params,
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
