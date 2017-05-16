import React from 'react';
import {shallow, mount} from 'enzyme';
import expect from 'expect';
import DateSelector from '../DateSelector';
import moment from 'moment';

const now = moment();
const oneYearFromNow = moment().add(1,"year");

function setup() {
  const props = {
    minDate: now,
    maxDate: oneYearFromNow,
    placeholderText: "Select date...",
    onChange: () => {}
  };
  return shallow(<DateSelector {...props} />);
}

describe('Rendering DateSelector ', () => {
  it('Renders Without Crashing (Shallow)', () => {
    shallow(<DateSelector placeholder="Select date..." />);
  });

  it('Renders Without Crashing (Full)', () => {
    mount(<DateSelector placeholder="Select date..." />);
  });

  it('Renders DateSelector', () => {
    const wrapper = setup();
    expect(wrapper.find('t').length).toBe(1);
  });

  it('Initial Values Of Date', () => {
    const wrapper = setup();
    expect(wrapper.find('t').props().minDate).toBe(now);
    expect(wrapper.find('t').props().maxDate).toBe(oneYearFromNow);
  });
});

