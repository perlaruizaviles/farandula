import { FlightSearchCommand } from '../models/flightSearchComand';
export interface IFlightConnector {
  url: string
	getAvailableFlights(type:FlightSearchCommand): any //hay que cambiar el tipo cuando se creen los modelos => list<itineraries> ?
}