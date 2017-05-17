"use strict";

const TravelportFlightManager = require('../../travelport_flight_manger');
const SearchCommand = require('../../search_command');
const describe = require('mocha').describe;
const expect = require('chai').expect;


describe('TravelportFlightManager', function(){

    describe('buildEnvelopeStringFromSearch',function () {

        let flightManager = new TravelportFlightManager();
        it("should create a valid Soap request XML message",function(){
            let search  = new SearchCommand(null);
            search.from("DFW")
                .to("CDG")
                .departingAt(new Date())
                .returningAt(new Date())
                .limitTo(10);

           let request = flightManager.buildEnvelopeStringFromSearch(search);

           expect(request).to.equal("<>");


        });

    });


});




