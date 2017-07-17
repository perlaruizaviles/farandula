import { ISeat } from './iSeat'
import { IFares } from './iFares'
export interface ISegment {
    key:string
    airplane_icon_path:string
    operatingAirlineCode:string
    operatingFlightNumber:string
    operatingAirlineName:string
    marketingAirlineCode:string
    marketingAirlineName:string
    marketingFlightNumber:string
    departureAirportCode:string
    departureTerminal:string
    departureDate:string
    arrivalAirportCode:string
    arrivalTerminal:string
    arrivalDate:Date
    airplane_data:string
    duration:number
    seatsAvailable:ISeat[]
    price:IFares
}