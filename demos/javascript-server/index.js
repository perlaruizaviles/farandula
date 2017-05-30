'use strict';

const Hapi = require('hapi');
const Wreck = require('wreck');
let api = {
  path: '/api'
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
    Wreck.get('https://new-farandula.herokuapp.com/api/airports?pattern=' + params["pattern"], (err, payload, res) => {
      reply('' + res);
    });
  }
});

server.route({
  method: 'GET',
  path: api.path + '/flights',
  handler: function (request, reply) {
    let params = request.query;
    let baseURLRequest = 'https://new-farandula.herokuapp.com/api/flights?'
      + 'departingAirportCodes=' + params["departingAirportCodes"]
      + '&departingDates=' + params["departingDates"]
      + '&departingTimes=' + params["departingTimes"]
      + '&arrivalAirportCodes=' + params["arrivalAirportCodes"]
      + '&type=' + params["type"]
      + '&passenger=' + params["passenger"]
      + '&cabin=' + params["cabin"]
      + '&limit' + params["limit"];

    if (params["type"] === "roundTrip") {
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