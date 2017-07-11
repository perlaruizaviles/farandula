class Airleg {
  private id:number
  private departureAirportCode:string
  private arrivalAirportCode:string
  private departingDate:number
  private arrivalDate:number
  private segments:Segment[]

  public getId(): number {
    return this.id
  }

  public setId(id:number): void {
    this.id = id
  }

  public getDepartureAirportCode(): string {
    return this.departureAirportCode
  }

  public setDepartureAirportCode(departureAirportCode:string): void {
    this.departureAirportCode = departureAirportCode
  }

  public getDepartingDate(): number {
    return this.departingDate
  }

  public setDepartingDate(departingDate:number): void {
    this.departingDate = departingDate
  }

  public getArrivalDate(): number {
    return this.arrivalDate
  }

  public setArrivalDate(arrivalDate:number): void {
    this.arrivalDate = arrivalDate
  }

  public getArrivalAirportCode(): string {
    return this.arrivalAirportCode
  }
  
  public setArrivalAirportCode(arrivalAirportCode:string): void {
    this.arrivalAirportCode = arrivalAirportCode
  }

}