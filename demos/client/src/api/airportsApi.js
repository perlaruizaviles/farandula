import axios from 'axios';

// This file mocks a web API by working with the hard-coded data below.
// It uses setTimeout to simulate the delay of an AJAX call.
// All calls return promises.
//const source =  data.airports.map(e => { return {title:e.city + ' - ' + e.iata , description:e.name}});

class AirportsApi {

    static searchAirport(query){
      return new Promise((resolve, reject) => {
        axios({
          method:'get',
          url:'https://farandula-java.herokuapp.com/api/airports',
          responseType:'json',
          params: {
            pattern: query
          }
        }).then(function(response) {
            const airports = response.data.content.map(e => { return {title:e.city + ' - ' + e.iata , description:e.name}});
            resolve(Object.assign([], airports));
          });
      });
    }
}

export default AirportsApi;
