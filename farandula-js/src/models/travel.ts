export interface Travel<T> {
  departureDate: T
  returnDate?: string
  returningDate?: string
  departureAirport: T
  arrivalAirport: T
  cabinClass: string
  flightType: string
}