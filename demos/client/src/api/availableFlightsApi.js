import * as endpoint from "./apiEndpoints";
import axios from "axios";
import {List} from "immutable";
import {handleRequestData} from "../util/handleRequestData";

class AvailableFlightsApi {

  static getAvailableFlights(search) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url: endpoint.AVAILABLE_FLIGHTS_URL,
        responseType: 'json',
        params: search,
      }).then((response) => {
        const flights = response.data;
        resolve(List(flights));
      }).catch(e => {
        reject(e);
      });
    });
  }
}

export default AvailableFlightsApi;
