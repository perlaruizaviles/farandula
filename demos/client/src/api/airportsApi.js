import * as api from './apiEndpoints';
import axios from 'axios';

class AirportsApi {

    static searchAirport(query){
      return new Promise((resolve, reject) => {
        axios({
          method:'get',
          url: api.AIRPORTS_URL,
          responseType:'json',
          params: {
            pattern: query
          }
        }).then((response) => {
            const airports = response.data.content.map(e => { return {title:e.city + ' - ' + e.iata , description:e.name}}).slice(0, 5);
            resolve(Object.assign([], airports));
          })
          .catch(e => {
            reject(e);
          });
      });
    }
}

export default AirportsApi;
