import * as actions from "../travelConfig";
import moment from "moment";

describe('actions', () => {

  describe('changeTravelType', function () {

    const travelType1 = 'roundTrip';
    const travelType2 = 'oneWay';

    it('should have a type of CHANGE_TRAVEL_TYPE', function () {
      expect(actions.changeTravelType().type).toEqual('CHANGE_TRAVEL_TYPE');
    });

    it('should pass on the roundTrip travel we pass in', function () {
      expect(actions.changeTravelType(travelType1).travelType).toEqual(travelType1);
    });

    it('should pass on the oneWay travel we pass in', function () {
      expect(actions.changeTravelType(travelType2).travelType).toEqual(travelType2);
    });
  });

  describe('changeTravelDate', function () {

    const dateType1 = 'depart';
    const dateType2 = 'return';
    const date = moment();

    it('should have a type of CHANGE_TRAVEL_DATE', function () {
      expect(actions.changeTravelDate().type).toEqual('CHANGE_TRAVEL_DATE');
    });

    it('should pass on the depart travel we pass in', function () {
      expect(actions.changeTravelDate(dateType1).dateType).toEqual(dateType1);
    });

    it('should pass on the return travel we pass in', function () {
      expect(actions.changeTravelDate(dateType2).dateType).toEqual(dateType2);
    });

    it('should pass on the travel date we pass in', function () {
      expect(actions.changeTravelDate(date).dateType).toEqual(date);
    });
  });

  describe('cabinChange', function () {

    const cabin = 'economy';

    it('should have a type of CHANGE_CABIN', function () {
      expect(actions.cabinChange().type).toEqual('CHANGE_CABIN');
    });

    it('should pass on the economy cabin we pass in', function () {
      expect(actions.cabinChange(cabin).cabin).toEqual(cabin);
    });
  });

  describe('changeTravelFrom', function () {

    const airport = {
      title: 'Mexico City - MEX',
      description: 'Licenciado Benito Juarez International Airport'
    };

    it('should have a type of CHANGE_TRAVEL_FROM', function () {
      expect(actions.changeTravelFrom().type).toEqual('CHANGE_TRAVEL_FROM');
    });

    it('should pass on the airport we pass in', function () {
      expect(actions.changeTravelFrom(airport).airport).toEqual(airport);
    });
  });

  describe('changeTravelTo', function () {

    const airport = {
      title: 'Mexico City - MEX',
      description: 'Licenciado Benito Juarez International Airport'
    };

    it('should have a type of CHANGE_TRAVEL_TO', function () {
      expect(actions.changeTravelTo().type).toEqual('CHANGE_TRAVEL_TO');
    });

    it('should pass on the airport we pass in', function () {
      expect(actions.changeTravelTo(airport).airport).toEqual(airport);
    });
  });

  describe('addDestiny', function () {

    it('should have a type of ADD_DESTINY', function () {
      expect(actions.addDestiny().type).toEqual('ADD_DESTINY');
    });
  });

  describe('removeDestiny', function () {

    it('should have a type of REMOVE_DESTINY', function () {
      expect(actions.removeDestiny().type).toEqual('REMOVE_DESTINY');
    });
  });

  describe('exchangeDestinations', function () {

    const from = {
      title: "Mexico City - MEX",
      description: "Licenciado Benito Juarez International Airport"
    };
    const to = undefined;

    it('should have a type of EXCHANGE_DESTINATIONS', function () {
      expect(actions.exchangeDestinations().type).toEqual('EXCHANGE_DESTINATIONS');
    });

    it('should pass on the from destination we pass in', function () {
      expect(actions.exchangeDestinations(from).from).toEqual(from);
    });

    it('should pass on the to destination we pass in', function () {
      expect(actions.exchangeDestinations(to).to).toEqual(to);
    });
  });

  describe('searchAirportSuccess', function () {

    const airports = [{
      title: 'Guadalajara - GDL',
      description: 'Don Miguel Hidalgo Y Costilla International Airport'
    }];

    it('should have a type of SEARCH_AIRPORT_SUCCESS', function () {
      expect(actions.searchAirportSuccess().type).toEqual('SEARCH_AIRPORT_SUCCESS');
    });

    it('should pass on the airport we pass in', function () {
      expect(actions.searchAirportSuccess(airports).airports).toEqual(airports);
    });
  });

  describe('searchAvailableFlightsSuccess', function () {

    it('should have a type of SEARCH_AVAILABLE_FLIGHTS_SUCCESS', function () {
      expect(actions.searchAvailableFlightsSuccess().type).toEqual('SEARCH_AVAILABLE_FLIGHTS_SUCCESS');
    });
  });

  describe('cleanTravelFrom', function () {

    it('should have a type of CLEAN_TRAVEL_FROM', function () {
      expect(actions.cleanTravelFrom().type).toEqual('CLEAN_TRAVEL_FROM');
    });
  });

  describe('cleanTravelTo', function () {
    it('should have a type of CLEAN_TRAVEL_TO', function () {
      expect(actions.cleanTravelTo().type).toEqual('CLEAN_TRAVEL_TO');
    });
  });
});