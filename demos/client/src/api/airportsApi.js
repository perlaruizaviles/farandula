import delay from './delay';
import data from '../data/airports.json'
import _ from 'lodash';

// This file mocks a web API by working with the hard-coded data below.
// It uses setTimeout to simulate the delay of an AJAX call.
// All calls return promises.
const source =  data.airports.map(e => { return {title:e.city + ' - ' + e.iata , description:e.name}});

class AirportsApi {

    static getAllAirports() {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(Object.assign([], source));
            }, delay);
        });
    }

    static searchAirport(query){
        return new Promise((resolve, reject) => {
            const re = new RegExp(_.escapeRegExp(query), 'i');
            const isMatch = (result) => {
                if(query !== ""){ return re.test(result.title);}
                return re.test(result.title);
            };
            resolve(Object.assign([], _.filter(source, isMatch).slice(0, 5)));
            }
        );
    }
}

export default AirportsApi;
