import { IPassenger } from '../../models/iPassenger'
import { SearchCommand } from '../../models/searchCommand'
import { ISearchAirleg } from '../../models/iSearchAirleg'
import { IFlightConnector } from '../iFlightConnector'
import { IItinerary } from '../../models/iItinerary'
import { FlightTypes } from '../../flightTypes';
import http = require("https")

export class TravelPortFlightConnector implements IFlightConnector {
	
  targetBranch = 'P105356'
  url: string = 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService'

  public getAvailableFlights(searchCommand:SearchCommand, callback:any): IItinerary {
    let _searchCommand = searchCommand
    switch(_searchCommand.Type) {
      case FlightTypes.ONEWAY: 
        return this.getOneWay(_searchCommand, callback)
      case FlightTypes.ROUNDTRIP: 
        return this.getRoundTrip(_searchCommand, callback)
      case FlightTypes.JAW:
        return this.getMultiCity(_searchCommand, callback)
      default: 
        throw new Error("type \'"+_searchCommand.Type+"\' not found")
    }
  }
	
	private execRequest(request: string, callback: any): any {
		let options = {
      "method": "POST",
      "hostname": "americas.universal-api.pp.travelport.com",
      "path": "/B2BGateway/connect/uAPI/AirService",
      "headers": {
        "authorization": "Basic VW5pdmVyc2FsIEFQSS91QVBJLTY1NjUyMzc4ODpuRWRlS1lFZ2EyZmdCdGFBajg0RXJmSjlr",
        "content-type": "text/xml",
      }
    }
    let parseXML = this.parseResponse
    let req = http.request(options, function (res) {
      var chunks = [] as Buffer[]
      res.on("data", function (chunk:Buffer) {
        chunks.push(chunk);
      })

      res.on("end", function () {
        let body = Buffer.concat(chunks, undefined)
        let response = parseXML(Buffer.concat(chunks, undefined).toString(), callback)
      })
    })

    req.write(request)
    req.end()
	}

  private getOneWay(searchCommand:SearchCommand, callback:any): any {
    var _searchCommand = searchCommand
    var request = this.getHeadRequest()
    + this.getAirlegRequest(_searchCommand.getAirleg(0))
    + this.getAirSearchModifiers(2)
    + this.getPassengerRequestSection(_searchCommand.Passengers)
    + this.getAirPricingModifiers(_searchCommand.Currency.toString())
    + this.getTailRequest()
    this.execRequest(request, callback)
  }

  private getRoundTrip(searchCommand:SearchCommand, callback:any): any {
    var _searchCommand = searchCommand

    var request = this.getHeadRequest()
      + this.getRoundAirlegRequest(_searchCommand.getAirleg(0))
      + this.getAirSearchModifiers(2)
      + this.getPassengerRequestSection(_searchCommand.Passengers)
      + this.getAirPricingModifiers(_searchCommand.Currency.toString())
      + this.getTailRequest()
    
    return this.execRequest(request, callback)
  }

  private getMultiCity(searchCommand:SearchCommand, callback:any): any {
    var _searchCommand = searchCommand
    var request = this.getHeadRequest()
    
    for (let depDate of _searchCommand.getAllAirlegs()) {
      request += this.getAirlegRequest(depDate)
    }

    request += this.getAirSearchModifiers(2)
      + this.getPassengerRequestSection(_searchCommand.Passengers)
      + this.getAirPricingModifiers(_searchCommand.Currency.toString())
      + this.getTailRequest()

    return this.execRequest(request, callback)
  }

  private getHeadRequest(): string {
    return `<?xml version="1.0" encoding="UTF-8"?>
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
          <soapenv:Header />
          <soapenv:Body>
              <air:LowFareSearchReq xmlns:air="http://www.travelport.com/schema/air_v34_0" AuthorizedBy="user" SolutionResult="true" TargetBranch="${this.targetBranch}" TraceId="trace">
                  <com:BillingPointOfSaleInfo xmlns:com="http://www.travelport.com/schema/common_v34_0" OriginApplication="UAPI" />`
  }

  private getTailRequest(): string {
    return `</air:LowFareSearchReq>
          </soapenv:Body>
      </soapenv:Envelope>`
  }
  
  private getAirlegRequest(searchAirleg:ISearchAirleg): string {
    return `<air:SearchAirLeg>
                <air:SearchOrigin>
                    <com:Airport xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="${searchAirleg.departureAirport}" />
                </air:SearchOrigin>
                <air:SearchDestination>
                    <com:Airport xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="${searchAirleg.arrivalAirport}" />
                </air:SearchDestination>
                <air:SearchDepTime PreferredTime="${searchAirleg.departureDate}" />
                <air:AirLegModifiers>
                    <air:PreferredCabins>
                        <com:CabinClass xmlns:com="http://www.travelport.com/schema/common_v34_0" Type="${searchAirleg.cabinClass}" />
                    </air:PreferredCabins>
                </air:AirLegModifiers>
            </air:SearchAirLeg>`
  }

  private getRoundAirlegRequest(searchAirlegs:ISearchAirleg): string {
    return `<air:SearchAirLeg>
                <air:SearchOrigin>
                    <com:Airport xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="${searchAirlegs.departureAirport}" />
                </air:SearchOrigin>
                <air:SearchDestination>
                    <com:Airport xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="${searchAirlegs.arrivalAirport}" />
                </air:SearchDestination>
                <air:SearchDepTime PreferredTime="${searchAirlegs.departureDate}" />
                <air:AirLegModifiers>
                    <air:PreferredCabins>
                        <com:CabinClass xmlns:com="http://www.travelport.com/schema/common_v34_0" Type="${searchAirlegs.cabinClass}" />
                    </air:PreferredCabins>
                </air:AirLegModifiers>
            </air:SearchAirLeg>
            <air:SearchAirLeg>
                <air:SearchOrigin>
                    <com:Airport xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="${searchAirlegs.arrivalAirport}" />
                </air:SearchOrigin>
                <air:SearchDestination>
                    <com:Airport xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="${searchAirlegs.departureAirport}" />
                </air:SearchDestination>
                <air:SearchDepTime PreferredTime="${searchAirlegs.returningDate}" />
                <air:AirLegModifiers>
                    <air:PreferredCabins>
                        <com:CabinClass xmlns:com="http://www.travelport.com/schema/common_v34_0" Type="${searchAirlegs.cabinClass}" />
                    </air:PreferredCabins>
                </air:AirLegModifiers>
            </air:SearchAirLeg>`
  }

  private getPassengerRequestSection(passengers:IPassenger[]): string {
		var passengersSection = ''
		for (let passenger of passengers) {
			passengersSection += `<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="${passenger.type}" Age="${passenger.age}" />`
		}
    return passengersSection
  }

  private getAirSearchModifiers(limit:number): string {
    return `<air:AirSearchModifiers MaxSolutions="${limit}">
          <air:PreferredProviders>
              <com:Provider xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="1G" />
          </air:PreferredProviders>
      </air:AirSearchModifiers>
      `
  }
  
  private getAirPricingModifiers(currencyType:string): string {
    return `<air:AirPricingModifiers xmlns:com="http://www.travelport.com/schema/common_v34_0" CurrencyType="${currencyType}" />`
  }

  public parseResponse(xmlResponse:string, callback:any): any {
    let parsedResponse: {}
    let _xmlResponse = xmlResponse
    let xml2js = require('xml2js')
    let util = require('util')
    parsedResponse = xml2js.parseString(_xmlResponse, (err:any, result:any) => {
      callback(result)
    })
  }
}