import * as endpoint from "./apiEndpoints";
import axios from "axios";
import {List} from "immutable";

const globalDistributedSystems = ['amadeus', 'sabre', 'travelport'];

class AvailableFlightsApi {

  static getAvailableFlights(search) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url: endpoint.AVAILABLE_FLIGHTS_URL,
        responseType: 'json',
        params: this.handleTravelType(search),
      }).then((response) => {
        const flights = response.data;
        resolve(List(flights));
      }).catch(e => {
        reject(e);
      });
    });
  }

  static handleTravelType(search) {

    const passenger = this.passengerAdapter(search.passenger);

    const GDS = this.getRandomGDS(0, 3);
    console.log(GDS);

    let params = {
      gds: "",
      departingAirportCodes: search.departureAirport,
      departingDates: search.departingDate,
      departingTimes: search.departingTime,
      arrivalAirportCodes: search.arrivalAirport,
      type: search.type,
      passenger: passenger,
      cabin: search.cabin,
      limit: search.limit
    };

    if (params.type === "roundTrip") {
      params.returnDates = search.arrivalDate;
      params.returnTimes = search.arrivalTime;
      return params;
    }

    if (params.type === "multiCity") {
      params.departingAirportCodes = search.departingAirports;
      params.arrivalAirportCodes = search.arrivalAirports;
      params.departingDates = search.departingDates;
      params.departingTimes = search.departingTimes;
      return params;
    }

    return params;
  }

  static passengerAdapter(passenger) {
    return `children:${passenger.get('child')},infants:${passenger.get('lap-infant')},infantsOnSeat:${passenger.get('seat-infant')},adults:${passenger.get('adults')}`;

  }

  static getRandomGDS(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    const index = Math.floor(Math.random() * (max - min)) + min;
    return globalDistributedSystems[index];
  }
}

export default AvailableFlightsApi;
