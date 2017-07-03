"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const oneWayTravel_1 = require("../../models/oneWayTravel");
const roundTripTravel_1 = require("../../models/roundTripTravel");
const http = require("https");
class TravelPortFlightConnector {
    constructor(flightType) {
        this.targetBranch = 'P105356';
        this.url = 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService';
        switch (flightType) {
            case 'oneWay':
                this.getAvailableFlights = this.getOneWay;
                break;
            case 'roundTrip':
                this.getAvailableFlights = this.getRoundTrip;
                break;
            default:
                throw new Error("flightType\'" + flightType + "\' not found");
        }
    }
    getAvails(travel) {
        switch (travel.flightType) {
            case 'oneWay': {
                return this.getOneWay(new oneWayTravel_1.OneWayTravel(travel));
            }
            case 'roundTrip': {
                return this.getRoundTrip(new roundTripTravel_1.RoundTripTravel(travel));
            }
            default: {
                throw "Flight-type " + travel.flightType + " not supported";
            }
        }
    }
    execRequest(body) {
        var options = {
            "method": "POST",
            "hostname": "americas.universal-api.pp.travelport.com",
            "path": "/B2BGateway/connect/uAPI/AirService",
            "headers": {
                "authorization": "Basic VW5pdmVyc2FsIEFQSS91QVBJLTY1NjUyMzc4ODpuRWRlS1lFZ2EyZmdCdGFBajg0RXJmSjlr",
                "content-type": "text/xml",
            }
        };
        var response = '';
        var req = http.request(options, function (res) {
            var chunks = [];
            res.on("data", function (chunk) {
                chunks.push(chunk);
            });
            res.on("end", function () {
                var body = Buffer.concat(chunks, undefined);
                console.log(body.toString());
                response = body.toString();
            });
        });
        req.write(body);
        req.end();
        return response;
    }
    getOneWay(travel) {
        var _travel = new oneWayTravel_1.OneWayTravel(travel);
        var request = this.getHeadRequest()
            + this.getAirlegRequest(_travel.departureAirport, _travel.arrivalAirport, _travel.departureDate, _travel.cabinClass)
            + this.getAirSearchModifiers(2)
            + this.getPassengerRequestSection()
            + this.getAirPricingModifiers('USD')
            + this.getTailRequest();
        return this.execRequest(request);
    }
    getRoundTrip(travel) {
        var request = this.getHeadRequest()
            + this.getAirlegRequest(travel.departureAirport, travel.arrivalAirport, travel.departureDate, travel.cabinClass)
            + this.getAirlegRequest(travel.arrivalAirport, travel.departureAirport, travel.returningDate, travel.cabinClass)
            + this.getAirSearchModifiers(2)
            + this.getPassengerRequestSection()
            + this.getAirPricingModifiers('USD')
            + this.getTailRequest();
        return this.execRequest(request);
    }
    getHeadRequest() {
        return `<?xml version="1.0" encoding="UTF-8"?>
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
          <soapenv:Header />
          <soapenv:Body>
              <air:LowFareSearchReq xmlns:air="http://www.travelport.com/schema/air_v34_0" AuthorizedBy="user" SolutionResult="true" TargetBranch="${this.targetBranch}" TraceId="trace">
                  <com:BillingPointOfSaleInfo xmlns:com="http://www.travelport.com/schema/common_v34_0" OriginApplication="UAPI" />`;
    }
    getTailRequest() {
        return `</air:LowFareSearchReq>
          </soapenv:Body>
      </soapenv:Envelope>`;
    }
    getAirlegRequest(departureAirport, arrivalAirport, departureDate, cabinClass) {
        return `<air:SearchAirLeg>
                      <air:SearchOrigin>
                          <com:Airport xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="${departureAirport}" />
                      </air:SearchOrigin>
                      <air:SearchDestination>
                          <com:Airport xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="${arrivalAirport}" />
                      </air:SearchDestination>
                      <air:SearchDepTime PreferredTime="${departureDate}" />
                      <air:AirLegModifiers>
                          <air:PreferredCabins>
                              <com:CabinClass xmlns:com="http://www.travelport.com/schema/common_v34_0" Type="${cabinClass}" />
                          </air:PreferredCabins>
                      </air:AirLegModifiers>
                  </air:SearchAirLeg>`;
    }
    getPassengerRequestSection() {
        return `<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="ADT" Age="0" />
            <com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="ADT" Age="0" />
            <com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="ADT" Age="0" />
            <com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="CHD" Age="8" />
            <com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="CHD" Age="9" />`;
    }
    getAirSearchModifiers(limit) {
        return `<air:AirSearchModifiers MaxSolutions="${limit}">
          <air:PreferredProviders>
              <com:Provider xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="1G" />
          </air:PreferredProviders>
      </air:AirSearchModifiers>
      `;
    }
    getAirPricingModifiers(currencyType) {
        return `<air:AirPricingModifiers xmlns:com="http://www.travelport.com/schema/common_v34_0" CurrencyType="${currencyType}" />`;
    }
}
exports.TravelPortFlightConnector = TravelPortFlightConnector;
//# sourceMappingURL=/home/alan/projects/farandula/farandula-js/connectors/travelport/travelportFlightConnector.js.map