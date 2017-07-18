import { Luisa } from '../luisa'
import { IItinerary } from '../models/iItinerary'
import { TravelPortFlightConnector } from '../connectors/travelport/travelportFlightConnector'
import { FlightTypes } from '../flightTypes'
import { IPassenger } from '../models/iPassenger'
import { CabinClass } from '../models/cabinClass'
import * as moment from 'moment'

describe('Luisa', () => {
  let passengers:IPassenger[] = [{ type:'ADT', age:50 }]
    let date1 = moment().add(10,"days").format('YYYY-mm-dd')

  // TODO: test that response is an ItineraryList
  test('OneWay call', done => {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 16000
    let luisa:Luisa = new Luisa()
    let travelport = new TravelPortFlightConnector()
    let listItineraries:IItinerary[] = luisa.use(travelport).getFlights()
      .setDepartureAirports(['HMO'])
      .setArrivalAirports(['MEX'])
      .setDepartingDates([date1])
      .setPreferenceClass(CabinClass.ECONOMY)
      .setCurrency('USD')
      .setType(FlightTypes.ONEWAY)
      .setLimit(50)
      .setPassengers(passengers)
      .execute((response:string) => {
        let envelope = response['SOAP:Envelope']['$']
        expect(envelope).not.toBe(null)
        done()
      })
      //expect(listItineraries).not.toBe(undefined)
  })
})