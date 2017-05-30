'use strict';

const Hapi = require('hapi');
const Wreck = require('wreck');
const api = {
  path: '/api'
};

const farandulaApi = {
  baseURL: 'https://new-farandula.herokuapp.com/api/',
  airports: 'airports',
  flights: 'flights'
};

const server = new Hapi.Server();

server.connection({
  host: 'localhost',
  port: process.env.PORT || 8000
});

server.route({
  method: 'GET',
  path: api.path + '/airports',
  handler: function (request, reply) {
    let params = request.query;
    Wreck.get(farandulaApi.baseURL + farandulaApi.airports + '?pattern=' + params["pattern"], (err, payload, res) => {
      reply('' + res);
    });
  }
});

server.route({
  method: 'GET',
  path: api.path + '/flights',
  handler: function (request, reply) {
    let params = request.query;
    let baseURLRequest = farandulaApi.baseURL + farandulaApi.flights + '?'
      + 'departingAirportCodes=' + params["departingAirportCodes"]
      + '&departingDates=' + params["departingDates"]
      + '&departingTimes=' + params["departingTimes"]
      + '&arrivalAirportCodes=' + params["arrivalAirportCodes"]
      + '&type=' + params["type"]
      + '&passenger=' + params["passenger"]
      + '&cabin=' + params["cabin"]
      + '&limit' + params["limit"];

    if (params["type"] === "round") {
      baseURLRequest += '&returnDates=' + params["returnDates"] + '&returnTimes=' + params["returnTimes"];
    }

    Wreck.get(baseURLRequest,
      (err, payload, res) => {
        reply('' + res);
      });
  }
});

server.start();
console.log("Server running on port: " + (process.env.PORT || 8000));