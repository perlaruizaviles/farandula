import { Travel } from './travel'

export class RoundTripTravel implements Travel<string> {
  departureDate: string
  returningDate: string
  departureAirport: string
  arrivalAirport: string
  cabinClass: string
  flightType: string

  constructor(travel:Travel<string>) {
    Object.assign(this, travel)
  }
}