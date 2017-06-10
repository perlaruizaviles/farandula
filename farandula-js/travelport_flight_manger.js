"use strict";
const fs = require('fs');
const moment = require('moment');
const config = require("./config.js");
const FlightType = require("./flightType.js");
const CabinClassType = require("./cabinClassType.js");
const http = require('http');

let formatDate = function(aDate){
    return moment(aDate).format('YYYY-MM-DD');
}

class TravelportFlightManager {

    constructor(search){
        this._search = search;
    }


    getAvail(){
        let response = this.buildAndSendRequestForAvail(this._search);
        return [];
    }

    buildAndSendRequestForAvail(search) {
        //create SOAP envelope
        let envelope = this.buildEnvelopeStringFromSearch(search);


        let url_api = "https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService";
        let soapResponse = this.sendRequest(envelope, url_api);

        return soapResponse;
    }


    getSearchAirLegs(search){
        let classTravel = !search.getCabinClass() ? CabinClassType.ECONOMY : search.getCabinClass();
        let legTemplate = fs.readFileSync("travelport/XML.request/requestSearchAirLeg.xml","utf8");

        let airlegs = "";

        let leg = legTemplate.replace("${departureAirport}", search.getDepartureAirportCode() );
        leg = leg.replace("${arrivalAirport}", search.getArrivalAirportCode() );
        leg = leg.replace("${departureDate}", formatDate(search.getDepartingDate()));


        airlegs += leg;
        if ( search.type == FlightType.ROUNDTRIP ){
            let leg2 = legTemplate.replace("departureAirport", search.getArrivalAirportCode());
            leg2 = leg2.replace("arrivalAirport", search.getDepartureAirportCode());
            leg2 = leg2.replace("departureDate", formatDate(search.getReturningDate()));

            airlegs += leg2;
        }
        airlegs = airlegs.replace('${classTravel}',classTravel);

        return airlegs;
    }

    buildEnvelopeStringFromSearch(search) {
        let header = fs.readFileSync("travelport/XML.request/requestHeader.xml","utf8");


        header = header.replace("${targetBranch}",config.travelport.targetBranch);

        let xml = this.getSearchAirLegs( search );

        let tail = fs.readFileSync("travelport/XML.request/requestTail.xml");
        return header + xml + tail;
    }

    sendRequest(envelope, url_api) {

    }
}

module.exports = TravelportFlightManager;