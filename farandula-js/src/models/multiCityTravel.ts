import { Travel } from './travel'

export class MultiCityTravel implements Travel<string[]> {
  departureDate: string[]
  departureAirport: string[]
  arrivalAirport: string[]
  cabinClass: string
  flightType: string

  constructor(travel:Travel<string[]>) {
    Object.assign(this, travel)
  }
}