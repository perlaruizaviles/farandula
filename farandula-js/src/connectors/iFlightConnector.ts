import { SearchCommand } from '../models/searchCommand'
export interface IFlightConnector {
  url: string
	getAvailableFlights(type:SearchCommand, callback:any): any //hay que cambiar el tipo cuando se creen los modelos => list<itineraries> ?
}