import { Travel } from '../models/travel'
export interface FlightConnector {
  url: string
	getAvails(travel:Travel<any>): any //hay que cambiar el tipo cuando se creen los modelos => list<itineraries> ?
}