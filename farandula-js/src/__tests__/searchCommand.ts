import { CabinClass } from '../models/cabinClass'
import { IPassenger } from '../models/iPassenger'
import { SearchCommand } from '../models/searchCommand'
import { TravelPortFlightConnector } from '../connectors/travelport/travelportFlightConnector'

describe('SearchCommand', () => {
  let passengers:IPassenger[] = [{ type:'ADT', age:50 }]
  test('Build one-way search command', () => {
    
    let expected:SearchCommand = {
      _flightConnector: {
        targetBranch: 'P105356',
        url: 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService' 
      },
      _departureAirports: ['HMO'],
      _arrivalAirports: ['MEX'],
      _passengers: [{ type: 'ADT', age:50}],
      _flightType: 'oneWay',
      _currency: 'USD',
      _offset: 50,
      _cabinClass: CabinClass.ECONOMY
    }

    let travelportConnector = new TravelPortFlightConnector()
    
    let actual:SearchCommand = new SearchCommand(travelportConnector)
      .setDepartureAirports(['HMO'])
      .setArrivalAirports(['MEX'])
      .setPassengers(passengers)
      .setType('oneWay')
      .setCurrency('USD')
      .setLimit(50)
      .setPreferenceClass(CabinClass.ECONOMY)
      
    console.log(actual)
    console.log('////')
    console.log(expected)
    expected(actual).toMatchObject(expected)
  })
})