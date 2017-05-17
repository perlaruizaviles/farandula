import * as actions from '../travelConfig';
import * as types from '../actionTypes';
import moment from 'moment';

describe('actions', () => {
  it('Should create an action to changeTravelType for roundTrip', () => {
    const value = 'roundTrip';
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
});