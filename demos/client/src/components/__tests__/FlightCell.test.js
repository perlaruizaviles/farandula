import React from 'react';
import {shallow} from 'enzyme';
import expect from 'expect';
import FlightCell from '../FlightCell';

function setup() {
  const props = {};
  return shallow(<FlightCell {...props} />);
}

describe('Rendering FlightCell ', () => {
  it('Renders FlightCell', () => {
    const wrapper = setup();
    expect(wrapper.find('PriceSection').length).toBe(1);
    expect(wrapper.find('FlightSection').length).toBe(1);
  });
});