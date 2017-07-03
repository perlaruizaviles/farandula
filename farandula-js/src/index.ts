import http = require("https")
import { Travel } from './models/travel'
import { OneWayTravel } from './models/oneWayTravel'
import { RoundTripTravel } from './models/roundTripTravel'
const { TravelPortFlightConnector } = require('./connectors/travelport/travelportFlightConnector')

export class Farandula {
  private connector:any

  constructor(private gds:string, private travel:Travel<any>) {
    this.setTravelStrategy()
  }

  private setTravelStrategy():void {
    var flightType = this.travel.flightType
    this.connector = new TravelPortFlightConnector(flightType)
  }

  public getAvailableFlights() {
    return this.connector.getAvailableFlights(this.travel)
  }
}