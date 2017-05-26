import * as endpoint from "./apiEndpoints";
import axios from "axios";
import {List} from "immutable";

class AvailableFlightsApi {

  static getAvailableFlights(search) {

    const passenger = `children:${search.passenger.get('child')},infants:${search.passenger.get('lap-infant')},infantsOnSeat:${search.passenger.get('seat-infant')},adults:${search.passenger.get('adults')}`;

    let params = {
      departingAirportCodes: search.departureAirport,
      departingDates: search.departingDate,
      departingTimes: search.departingTime,
      arrivalAirportCodes: search.arrivalAirport,
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
        url: endpoint.TEMP_AVAILABLE_FLIGHTS_URL,
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
