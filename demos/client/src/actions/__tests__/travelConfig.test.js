import * as actions from "../travelConfig";
import * as types from "../actionTypes";
import moment from "moment";

describe('actions', () => {
  it('Should create an action to changeTravelType for roundTrip', () => {
    const value = 'round';
    const expectedAction = {
      type: types.CHANGE_TRAVEL_TYPE,
      value
    };
    expect(actions.changeTravelType(value)).toEqual(expectedAction);
  });

  it('Should create an action to changeTravelType for oneWay', () => {
    const value = 'oneWay';
    const expectedAction = {
      type: types.CHANGE_TRAVEL_TYPE,
      value
    };
    expect(actions.changeTravelType(value)).toEqual(expectedAction);
  });

  it('Should create an action to changeTravelDate for depart', () => {
    const dateType = 'depart';
    const date = moment();
    const expectedAction = {
        type: types.CHANGE_TRAVEL_DATE,
        dateType,
        date
    };
    expect(actions.changeTravelDate(dateType, date)).toEqual(expectedAction);
  });

  it('Should create an action to changeTravelDate for return', () => {
    const dateType = 'return';
    const date = moment();
    const expectedAction = {
        type: types.CHANGE_TRAVEL_DATE,
        dateType,
        date
    };
    expect(actions.changeTravelDate(dateType, date)).toEqual(expectedAction);
  });

  it('Should create an action to travelerTypeCountChange', () => {
    const typeTraveler = 'adults';
    const count = 1;
    const expectedAction = {
        type: types.CHANGE_TRAVELER_TYPE_COUNT,
        typeTraveler,
        count
    };
    expect(actions.travelerTypeCountChange(typeTraveler, count)).toEqual(expectedAction);
  });

  it('Should create an action to cabinChange', () => {
    const cabin = 'economy';
    const expectedAction = {
        type: types.CHANGE_CABIN,
        cabin
    };
    expect(actions.cabinChange(cabin)).toEqual(expectedAction);
  });

  it('Should create an action to changeTravelFrom', () => {
    const airport = {title: 'Mexico City - MEX', description: 'Licenciado Benito Juarez International Airport'};
    const expectedAction = {
        type: types.CHANGE_TRAVEL_FROM,
        airport
    };
    expect(actions.changeTravelFrom(airport)).toEqual(expectedAction);
  });

  it('Should create an action to changeTravelTo', () => {
    const airport = {title: 'Mexico City - MEX', description: 'Licenciado Benito Juarez International Airport'};
    const expectedAction = {
        type: types.CHANGE_TRAVEL_TO,
        airport
    };
    expect(actions.changeTravelTo(airport)).toEqual(expectedAction);
  });

  it('Should create an action to exchangeDestinations', () => {
    const from = {title: "Mexico City - MEX", description: "Licenciado Benito Juarez International Airport"};
    const to = {title: "Mexicali - MXL", description: "General Rodolfo Se1nchez Taboada International Airport"};
    const expectedAction = {
        type: types.EXCHANGE_DESTINATIONS,
        from,
        to
    };
    expect(actions.exchangeDestinations(from, to)).toEqual(expectedAction);
  });

  it('Should create an action to searchAirportSuccess', () => {
    const airports = [{title: 'Guadalajara - GDL', description: 'Don Miguel Hidalgo Y Costilla International Airport'}];
    const expectedAction = {
        type: types.SEARCH_AIRPORT_SUCCESS,
        airports
    };
    expect(actions.searchAirportSuccess(airports)).toEqual(expectedAction);
  });

  it('Should create an action to cleanTravelFrom', () => {
    const expectedAction = {
        type: types.CLEAN_TRAVEL_FROM
    };
    expect(actions.cleanTravelFrom()).toEqual(expectedAction);
  });

  it('Should create an action to cleanTravelTo', () => {
    const expectedAction = {
        type: types.CLEAN_TRAVEL_TO
    };
    expect(actions.cleanTravelTo()).toEqual(expectedAction);
  });
});
