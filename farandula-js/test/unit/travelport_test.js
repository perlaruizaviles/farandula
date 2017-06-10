
//TODO research how to use import instead of require
const describe = require('mocha').describe;
const before = require('mocha').before;
const expect = require('chai').expect;
const Luisa  = require('../../luisa.js');
const TravelportFlightManager  = require('../../travelport_flight_manger.js');



describe('Travelport', function () {
    before(function () {
        Luisa.flightManager = new TravelportFlightManager();
    });

    describe('Luisa with round trip search', function () {


        it.skip('should find avail', function () {
            let flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(new Date())
                .returningAt(new Date())
                .limitTo(10)
                .execute();

           expect(flights).to.not.empty;

              
        })
    })

});
