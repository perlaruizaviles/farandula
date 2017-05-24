import React from 'react';
import {shallow} from 'enzyme';
import DateSelector from '../Common/DateSelector';
import moment from 'moment';

describe('Rendering DateSelector', () => {

  const now = moment("2017-05-22T13:48:18.677");
  
  const oneYearFromNow = moment("2018-05-22T13:48:18.677");

  const props = {
    minDate: now,
    maxDate: oneYearFromNow,
    startDate: now,
    endDate: now,
    selected: now,
    changeTravelDate: () => {}
  };

  const tree = shallow(<DateSelector {...props} />);

  it('Should create an snapshot for DateSelector', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders DateSelector', () => {
    expect(tree.find('t').length).toBe(1);
  });

  it('Min date', () => {
    expect(tree.find('t').props().minDate).toBe(now);
  });

  it('Max date', () => {
    expect(tree.find('t').props().maxDate).toBe(oneYearFromNow);
  });
});
