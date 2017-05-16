import * as actions from '../travelConfig';
import * as types from '../actionTypes';
import moment from 'moment';

describe('actions', () => {
  it('Should create an action to changeTravelType', () => {
    const value = 'round-trip'
    const expectedAction = {
      type: types.CHANGE_TRAVEL_TYPE,
      value
    }
    expect(actions.changeTravelType(value)).toEqual(expectedAction)
  })

  it('Should create an action to changeTravelDate', () => {
    const dateType = 'depart'
    const date = moment()
    const expectedAction = {
        type: types.CHANGE_TRAVEL_DATE,
        dateType,
        date 
    }
    expect(actions.changeTravelDate(dateType, date)).toEqual(expectedAction)
  })
})