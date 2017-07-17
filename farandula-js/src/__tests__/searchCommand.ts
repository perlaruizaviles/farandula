import { CabinClass } from '../models/cabinClass'
import { IPassenger } from '../models/iPassenger'
import { SearchCommand } from '../models/searchCommand'
import { TravelPortFlightConnector } from '../connectors/travelport/travelportFlightConnector'
import { FlightTypes } from '../flightTypes';

describe('SearchCommand', () => {
  let passengers:IPassenger[] = [{ type:'ADT', age:50 }]
  test('Build one-way search command', () => {
    let expected = {
      _departureAirports:['HMO'],
      _arrivalAirports:['MEX'],
      _departingDates:['2017-08-24'],
      _cabinClass:CabinClass.ECONOMY,
      _currency:'USD',
      _flightConnector: new TravelPortFlightConnector(),
      _flightType:FlightTypes.ONEWAY,
      _returningDate: undefined,
      _limit:50,
      _passengers:passengers
    }

    let travelportConnector = new TravelPortFlightConnector()
    let actual:SearchCommand = new SearchCommand(travelportConnector)
      .setDepartureAirports(['HMO'])
      .setArrivalAirports(['MEX'])
      .setDepartingDates(['2017-08-24'])
      .setPreferenceClass(CabinClass.ECONOMY)
      .setCurrency('USD')
      .setType(FlightTypes.ONEWAY)
      .setLimit(50)
      .setPassengers(passengers)
      
    expect(actual).toEqual(expected)
  })
})