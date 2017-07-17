import { IPrice } from './iPrice'
import { ISegment } from './iSegment'
export interface IAirleg {
  id:number
  departureAirportCode:string
  arrivalAirportCode:string
  departingDate:number
  arrivalDate:number
  price: IPrice
  segments:ISegment[]
}