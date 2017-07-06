import { FlightSearchCommand } from './models/flightSearchComand';
import { IFlightConnector } from './connectors/iFlightConnector';
import { TravelPortFlightConnector } from './connectors/travelport/travelportFlightConnector';
import http = require("https")

export class Farandula {
  private connector:IFlightConnector

  constructor(private gds:string, private flightSearchCommand:FlightSearchCommand) {
    this.setTravelStrategy()
  }

  private setTravelStrategy():void {
    var flightType = this.flightSearchCommand.type
    this.connector = new TravelPortFlightConnector(flightType)
  }

  public getAvailableFlights() {
    console.log(this.flightSearchCommand)
    return this.connector.getAvailableFlights(this.flightSearchCommand)
  }
}