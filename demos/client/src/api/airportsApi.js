import * as endpoint from "./apiEndpoints";
import axios from "axios";

class AirportsApi {

  static searchAirport(query) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url: endpoint.AIRPORTS_URL, //TODO: Change to AIRPORTS_URL when backend work well again
        responseType: 'json',
        params: {
          pattern: query
        }
      }).then((response) => {
        const airports = response.data.map(e => {
          return {title: e.city + ' - ' + e.iata, description: e.name}
        }).slice(0, 5);
        resolve(Object.assign([], airports));
      })
        .catch(e => {
          reject(e);
        });
    });
  }
}

export default AirportsApi;
