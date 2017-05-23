import React from "react";
import {shallow} from "enzyme";
import expect from "expect";
import DateSelector from "../Common/DateSelector";
import moment from "moment";

const now = moment();
const oneYearFromNow = moment().add(1, "year");

function setup() {
  const props = {
    minDate: now,
    maxDate: oneYearFromNow,
    startDate: now,
    endDate: now,
    selected: now,
    changeTravelDate: () => {
    }
  };
  return shallow(<DateSelector {...props} />);
}

describe('Rendering DateSelector ', () => {
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