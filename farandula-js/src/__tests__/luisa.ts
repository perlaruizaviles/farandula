import { listenerCount } from 'cluster';

import { Luisa } from '../luisa';
import { IItinerary } from '../models/iItinerary';
import { TravelPortFlightConnector } from '../connectors/travelport/travelportFlightConnector';
import { FlightTypes } from '../flightTypes';
import { IPassenger } from '../models/iPassenger';
import { CabinClass } from '../models/cabinClass';

describe('Luisa', () => {
  let passengers:IPassenger[] = [{ type:'ADT', age:50 }]
  test('OneWay call', done => {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 16000
    let luisa:Luisa = new Luisa()
    let travelport = new TravelPortFlightConnector()
    let listItineraries:IItinerary[] = luisa.use(travelport).getFlights()
      .setDepartureAirports(['HMO'])
      .setArrivalAirports(['MEX'])
      .setDepartingDates(['2017-08-24'])
      .setPreferenceClass(CabinClass.ECONOMY)
      .setCurrency('USD')
      .setType(FlightTypes.ONEWAY)
      .setLimit(50)
      .setPassengers(passengers)
      .execute((response:string) => {
        console.log(response)
        done()
      })
      expect(listItineraries).not.toBe(undefined)
  })
})