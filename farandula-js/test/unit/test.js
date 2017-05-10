const chai = require('chai');
const expect = chai.expect;
const Luisa  = require('../../luisa.js');
describe('Travelport', function () {
    describe('Luisa with round trip search', function () {
        it('should find avail', function () {

            let flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(returningDate)
                .limitTo(limit)
                .execute();

           expect(flights)

              
        })
    })

});
