import { Passenger } from './passenger'
import { ISearchAirleg } from './iSearchAirleg';
import { IFlightConnector } from '../connectors/iFlightConnector';
export class FlightSearchCommand {
  private departureAirports: string[]
  private arrivalAirports: string[]
  private departingDates: string[]
  private returnDate: string
  private passengers: Passenger[]
  private flightType: FlightTypes
  private flightConnector: IFlightConnector
  
  //default values
  private currency: string = 'USD'
  private offset: number = 50
  private cabinClass: CabinClassType = CabinClassType.ECONOMY

  constructor(flightConnector:IFlightConnector) {
    this.flightConnector = flightConnector
  }

  public from(departureAirports: string[]): FlightSearchCommand {
    this.departureAirports = departureAirports
    return this
  }

  public to(arrivalAirports: string[]): FlightSearchCommand {
    this.arrivalAirports = arrivalAirports
    return this
  }

  public departureDates(departingDates:string[]): FlightSearchCommand {
    this.departingDates = departingDates
    return this
  }

  public returningDate(returningDate:string): FlightSearchCommand {
    this.returnDate = returningDate
    return this
  }

  public forPassengers(passengers:Passenger[]): FlightSearchCommand {
    if (passengers.length > 6) {
      throw new Error("Is not possible to search for more than 6 passengers")
    } else {
      this.passengers = passengers
    }
    return this
  }

  public limit(limit:number): FlightSearchCommand {
    this.offset = limit
    return this
  }

  public type(type:string): FlightSearchCommand {
    this.flightType = type
    return this
  }

  public preferenceClass(preferenceClass:CabinClassType): FlightSearchCommand {
    this.cabinClass = preferenceClass
    return this
  }

  public execute(callback:any): Itinerary[] {
    return this.flightConnector.getAvailableFlights(this, callback)
  }

  public getDepartureAirports(): string[] {
    return this.departureAirports
  }

  public getArrivalAirports(): string[] {
    return this.arrivalAirports
  }

  public getDepartingDates(): string[] {
    return this.departingDates
  }

  public getReturningDate(): string {
    return this.returnDate
  }

  public getPassengers(): Passenger[] {
    return this.passengers
  }

  public getType(): FlightTypes {
    return this.flightType
  }

  public getOffSet(): number {
    return this.offset
  }

  public getCabinClass(): CabinClassType {
    return this.cabinClass
  }

  public getCurrency(){
    return this.currency
  }

  public getAirleg(index:number): ISearchAirleg {
    let searchAirleg:ISearchAirleg = {
      departureDate:      this.departingDates[index],
      departureAirport:   this.departureAirports[index],
      arrivalAirport:     this.arrivalAirports[index],
      returningDate:      this.returnDate,
      cabinClass:         this.cabinClass
    }
    return searchAirleg
  }

  public getAllAirlegs(): ISearchAirleg[] {
    let numAirlegs = this.departureAirports.length
    let airlegs:ISearchAirleg[] = []
    for (var key in this.departureAirports) {
      console.log(key)
      let newAirleg: ISearchAirleg = {
        departureDate:    this.departingDates[key],
        departureAirport: this.departureAirports[key],
        arrivalAirport:   this.arrivalAirports[key],
        returningDate:    this.returnDate,
        cabinClass:       this.cabinClass
      }
      airlegs.push(newAirleg)
    }
    return airlegs
  }
}