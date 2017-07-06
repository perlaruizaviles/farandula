import { Passenger } from './passenger'
import { ISearchAirleg } from './iSearchAirleg';
export class FlightSearchCommand {
  searchAirleg: ISearchAirleg[]
  currency: string = 'USD'
  type: string
  limit: number
	passengers: Passenger[]

  constructor(flightSearchCommand:{searchAirleg: ISearchAirleg[], currency: string, type: string, limit: number, passengers:Passenger[]}){
    Object.assign(this, flightSearchCommand)
  }
}