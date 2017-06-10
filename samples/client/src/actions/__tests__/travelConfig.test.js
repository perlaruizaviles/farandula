import * as actions from "../travelConfig";

describe('actions', () => {

  describe('changeTravelType', function() {

    const travelType1 = 'roundTrip';
    const travelType2 = 'oneWay';

    it('should have a type of CHANGE_TRAVEL_TYPE', function() {
      expect(actions.changeTravelType().type).toEqual('CHANGE_TRAVEL_TYPE');
    });

    it('should pass on the roundTrip travel we pass in', function () {
      expect(actions.changeTravelType(travelType1).travelType).toEqual(travelType1);
    });

    it('should pass on the oneWay travel we pass in', function() {
      expect(actions.changeTravelType(travelType2).travelType).toEqual(travelType2);
    });
  });

  describe('cabinChange', function() {

    const cabin = 'economy';

    it('should have a type of CHANGE_CABIN', function() {
      expect(actions.cabinChange().type).toEqual('CHANGE_CABIN');
    });

    it('should pass on the economy cabin we pass in', function() {
      expect(actions.cabinChange(cabin).cabin).toEqual(cabin);
    });
  });

  describe('addDestiny', function() {

    it('should have a type of ADD_DESTINY', function() {
      expect(actions.addDestiny().type).toEqual('ADD_DESTINY');
    });
  });

  describe('removeDestiny', function() {

    it('should have a type of REMOVE_DESTINY', function() {
      expect(actions.removeDestiny().type).toEqual('REMOVE_DESTINY');
    });
  });

  describe('searchAirportSuccess', function() {

    const airports = [{
      title: 'Guadalajara - GDL',
      description: 'Don Miguel Hidalgo Y Costilla International Airport'
    }];

    it('should have a type of SEARCH_AIRPORT_SUCCESS', function() {
      expect(actions.searchAirportSuccess().type).toEqual('SEARCH_AIRPORT_SUCCESS');
    });

    it('should pass on the airport we pass in', function() {
      expect(actions.searchAirportSuccess(airports).airports).toEqual(airports);
    });
  });

  describe('searchAvailableFlightsSuccess', function() {

    it('should have a type of SEARCH_AVAILABLE_FLIGHTS_SUCCESS', function() {
      expect(actions.searchAvailableFlightsSuccess().type).toEqual('SEARCH_AVAILABLE_FLIGHTS_SUCCESS');
    });
  });

  describe('cleanTravelFrom', function() {

    it('should have a type of CLEAN_TRAVEL_FROM', function() {
      expect(actions.cleanTravelFrom().type).toEqual('CLEAN_TRAVEL_FROM');
    });
  });

  describe('cleanTravelTo', function() {
    it('should have a type of CLEAN_TRAVEL_TO', function() {
      expect(actions.cleanTravelTo().type).toEqual('CLEAN_TRAVEL_TO');
    });
  });
});