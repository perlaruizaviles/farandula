import { IFlightConnector } from './connectors/iFlightConnector'
import { SearchCommand } from './models/searchCommand'

export class Luisa {
  private flightConnector:IFlightConnector
  public use(flightConnector:IFlightConnector): Luisa {
    this.flightConnector = flightConnector
    return this
  }

  public getFlights(): SearchCommand {
    return new SearchCommand(this.flightConnector)
  }
}