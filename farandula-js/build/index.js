"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const { TravelPortFlightConnector } = require('./connectors/travelport/travelportFlightConnector');
class Farandula {
    constructor(gds, travel) {
        this.gds = gds;
        this.travel = travel;
        this.setTravelStrategy();
    }
    setTravelStrategy() {
        var flightType = this.travel.flightType;
        this.connector = new TravelPortFlightConnector(flightType);
    }
    getAvailableFlights() {
        return this.connector.getAvailableFlights(this.travel);
    }
}
exports.Farandula = Farandula;
//# sourceMappingURL=/home/alan/projects/farandula/farandula-js/index.js.map