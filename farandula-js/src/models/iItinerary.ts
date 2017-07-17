import { IAirleg } from './iAirleg'
import { IPrice } from './iPrice'
export interface IItinerary {
  id: number
  airlegs:IAirleg
  price:IPrice
}