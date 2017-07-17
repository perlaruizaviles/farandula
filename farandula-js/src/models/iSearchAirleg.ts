import { CabinClass } from './cabinClass'
export interface ISearchAirleg {
  departureDate: string
  returningDate?: string
  departureAirport: string
  arrivalAirport: string
  cabinClass: CabinClass
}