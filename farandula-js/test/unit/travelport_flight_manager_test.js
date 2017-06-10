"use strict";

const TravelportFlightManager = require('../../travelport_flight_manger');
const SearchCommand = require('../../search_command');
const describe = require('mocha').describe;
const moment =require('moment');
const chai =require('chai');
const expect = chai.expect;

chai.use(require('chai-string'));

describe('TravelportFlightManager', function(){

    describe('buildEnvelopeStringFromSearch',function () {
        let flightManager = new TravelportFlightManager();
        it("should create a valid Soap request XML message",function(){
            let departDate = moment('2017-07-07')
            let search  = (new SearchCommand(null)).from("DFW")
                .to("CDG")
                .departingAt(departDate.toDate())
                .returningAt(departDate.add(1, 'days').toDate())
                .limitTo(10);

           let request = flightManager.buildEnvelopeStringFromSearch(search);

            let expectedRequest = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">'
                +'    <soapenv:Header/>'
                +'<soapenv:Body>'
                +'<air:AvailabilitySearchReq xmlns:air="http://www.travelport.com/schema/air_v34_0" AuthorizedBy="user" TargetBranch="P105356" TraceId="trace">'
                +'    <com:BillingPointOfSaleInfo xmlns:com="http://www.travelport.com/schema/common_v34_0" OriginApplication="UAPI"/><air:SearchAirLeg>'
                +'<air:SearchOrigin>'
                +'<com:Airport Code="DFW" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>'
                +'    </air:SearchOrigin>'
                +'<air:SearchDestination>'
                +'<com:Airport Code="CDG" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>'
                +'    </air:SearchDestination>'
                +'<air:SearchDepTime PreferredTime="2017-07-07"/>'
                +'    <air:AirLegModifiers>'
                +'<air:PreferredCabins>'
                +'<com:CabinClass Type="ECONOMY" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>'
                +'    </air:PreferredCabins>'
                +'</air:AirLegModifiers>'
                +'</air:SearchAirLeg></air:AvailabilitySearchReq>'
                +'</soapenv:Body>'
                +'</soapenv:Envelope>';


            expect(request).to.equalIgnoreSpaces(expectedRequest);


        });

    });


});






