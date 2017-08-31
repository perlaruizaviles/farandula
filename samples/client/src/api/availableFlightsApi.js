import * as endpoint from "./apiEndpoints";
import axios from "axios";
import {List} from "immutable";

class AvailableFlightsApi {

  static getAvailableFlights(search) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url: endpoint.AVAILABLE_FLIGHTS_URL,
        responseType: 'json',
        params: search,
      }).then((response) => {
        debugger
          const flights = response.data;
        resolve(List(flights));
      }).catch(e => {
        reject(e);
      });
    });
  }
}

export default AvailableFlightsApi;
