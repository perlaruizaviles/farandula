import { FlightTypes } from '../flightTypes';
import { IPassenger } from './iPassenger'
import { ISearchAirleg } from './iSearchAirleg'
import { IFlightConnector } from '../connectors/iFlightConnector'
import { CabinClass } from './cabinClass'
import { IItinerary } from './iItinerary'

export class SearchCommand {
  private _departureAirports: string[]
  private _arrivalAirports: string[]
  private _departingDates: string[]
  private _returnDate: string
  private _passengers: IPassenger[]
  private _flightType: FlightTypes
  
  //default values
  private _currency: string = 'USD'
  private _limit: number = 50
  private _cabinClass: CabinClass = CabinClass.ECONOMY

  constructor(private _flightConnector:IFlightConnector) {}

  public setDepartureAirports(departureAirports: string[]): SearchCommand {
    this._departureAirports = departureAirports
    return this
  }

  public setArrivalAirports(arrivalAirports: string[]): SearchCommand {
    this._arrivalAirports = arrivalAirports
    return this
  }

  public setDepartingDates(departingDates:string[]): SearchCommand {
    this._departingDates = departingDates
    return this
  }

  public setReturningDate(returningDate:string): SearchCommand {
    this._returnDate = returningDate
    return this
  }

  public setPassengers(passengers:IPassenger[]): SearchCommand {
    if (passengers.length > 6) {
      throw new Error('Is not possible to search for more than 6 passengers')
    } else if (passengers.length <=0) {
      throw new Error('Should be at least one passenger')
    } else {
      this._passengers = passengers
    }
    return this
  }

  public setLimit(limit:number): SearchCommand {
    if (limit <=0) {
      throw new Error('Limit cannot be 0 or negative')
    } else {
      this._limit = limit
    }
    return this
  }

  public setType(type:FlightTypes): SearchCommand {
    this._flightType = type
    return this
  }

  public setPreferenceClass(preferenceClass:CabinClass): SearchCommand {
    this._cabinClass = preferenceClass
    return this
  }
  
  public setCurrency(currency:string): SearchCommand {
    this._currency = currency
    return this
  }

  public execute(callback:any): IItinerary[] {
    this.verifyObjectBuilt()
    return this._flightConnector.getAvailableFlights(this, callback)
  }

  public get DepartureAirports(): string[] {
    return this._departureAirports
  }

  public get ArrivalAirports(): string[] {
    return this._arrivalAirports
  }

  public get DepartingDates(): string[] {
    return this._departingDates
  }

  public get ReturningDate(): string {
    return this._returnDate
  }

  public get Passengers(): IPassenger[] {
    return this._passengers
  }

  public get Type(): FlightTypes {
    return this._flightType
  }

  public get Limit(): number {
    return this._limit
  }

  public get PreferenceClass(): CabinClass {
    return this._cabinClass
  }

  public get Currency(): string{
    return this._currency
  }

  public getAirleg(index:number): ISearchAirleg {
    let searchAirleg:ISearchAirleg = {
      departureDate:      this._departingDates[index],
      departureAirport:   this._departureAirports[index],
      arrivalAirport:     this._arrivalAirports[index],
      returningDate:      this._returnDate,
      cabinClass:         this._cabinClass
    }
    return searchAirleg
  }

  public getAllAirlegs(): ISearchAirleg[] {
    let numAirlegs = this._departureAirports.length
    let airlegs:ISearchAirleg[] = []
    for (var key in this._departureAirports) {
      console.log(key)
      let newAirleg: ISearchAirleg = {
        departureDate:    this._departingDates[key],
        departureAirport: this._departureAirports[key],
        arrivalAirport:   this._arrivalAirports[key],
        returningDate:    this._returnDate,
        cabinClass:       this._cabinClass
      }
      airlegs.push(newAirleg)
    }
    return airlegs
  }

  public verifyObjectBuilt(): boolean {
    if (this._arrivalAirports == undefined) throw new Error('\'arrivalAirports\' missing')
    if (this._departingDates == undefined) throw new Error('\'departingDates\' missing')
    if (this._departureAirports == undefined) throw new Error('\'departureAirports\' missing')
    if (this._flightType == undefined) throw new Error('\'flightType missing\'')
    if (this._passengers == undefined) throw new Error('\'passengers\' missing')
    if (this._flightType == FlightTypes.ROUNDTRIP && this._returnDate == undefined) throw new Error('\'returnDate\' missing')
    return true
  }
}