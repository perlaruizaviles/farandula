import { Travel } from './travel'
import { Passenger } from './passenger'

export class RoundTripTravel implements Travel<string> {
  departureDate: string
  returningDate: string
  departureAirport: string
  arrivalAirport: string
  cabinClass: string
  flightType: string
  passengers: Passenger[]

  constructor(travel:Travel<string>) {
    Object.assign(this, travel)
  }
}