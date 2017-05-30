/**
 * Created by antoniohernandez on 5/29/17.
 */
//1.
var Hapi = require('hapi');
const Wreck = require('wreck');
var api = {
    path:'/api'
}

//2.
const server = new Hapi.Server();
server.connection({
    host: 'localhost',
    port: 8000
});

// Add the route
server.route({
    method: 'GET',
    path: api.path+'/airports',
    handler: function (request, reply) {
        var params =  request.query
        Wreck.get('https://new-farandula.herokuapp.com/api/airports?pattern='+params["pattern"], (err, payload, res) => {
            reply(''+res);

        });


    }
});

server.route({
    method: 'GET',
    path: api.path+'/flights',
    handler: function (request, reply) {
        var params =  request.query
        var baseURLRequest = 'https://new-farandula.herokuapp.com/api/flights?'
            +'departingAirportCodes='+params["departingAirportCodes"]
            +'&departingDates='+params["departingDates"]
            +'&departingTimes='+params["departingTimes"]
            +'&arrivalAirportCodes='+params["arrivalAirportCodes"]
            +'&type='+params["type"]
            +'&passenger='+params["passenger"]
            +'&cabin='+params["cabin"]
            +'&limit'+params["limit"]

        if (params["type"]=="roundTrip") {
            baseURLRequest += '&returnDates='+params["returnDates"]+'&returnTimes='+params["returnTimes"];
        }

        Wreck.get(baseURLRequest,
            (err, payload, res) => {
            reply(''+res);

    });


    }
});

//4.
server.start();


