import { IFlightConnector } from './connectors/iFlightConnector';
import { FlightSearchCommand } from './models/flightSearchComand';

class Luisa {

  private flightConnector:IFlightConnector
  
  public use(flightConnector:IFlightConnector): void {
    this.flightConnector = flightConnector
  }

  public getFlights(): FlightSearchCommand {
    return new FlightSearchCommand(this.flightConnector)
  }
}