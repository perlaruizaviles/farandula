import { FlightSearchCommand } from '../models/flightSearchComand';
import { TravelPortFlightConnector } from '../connectors/travelport/travelportFlightConnector';
import { IFlightConnector } from '../connectors/iFlightConnector';

var passengers = [
      {type:'ADT', age:27},
      {type:'CHD', age:4}
    ]

var date1 = "2017-08-13"
var date2 = "2017-08-15"

describe('Tests FlightSearchCommand', () => {
  test('Build flightSearchCommand (ONEWAY)', () => {
    console.log(CabinClassType.ECONOMY)
    let travelportConnector:IFlightConnector = new TravelPortFlightConnector()
    let actual = new FlightSearchCommand(travelportConnector)
    actual.from(["HMO"])
          .to(["MEX"])
          .departureDates([date1])
          .forPassengers(passengers)
          .type(FlightTypes.ONEWAY)

    let expected = {
      departureAirports:  ["HMO"],
      arrivalAirports:    ["MEX"],
      departingDates:     date1,
      forPassengers:      passengers,
      flightType:         FlightTypes.ONEWAY
    }

    console.log(actual)
    console.log('...')
    console.log(expected)

    expect(actual).toMatchObject(expected)
  })
})