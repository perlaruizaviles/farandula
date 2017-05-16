import React from 'react';
import {shallow} from 'enzyme';
import expect from 'expect';
import FlightCell from '../FlightCell';
import FlightResults from '../FlightResults';

function setup() {
  const props = {};
  return shallow(<FlightResults {...props} />);
}

describe('Rendering FlightResults ', () => {
  it('Renders FlightResults', () => {
    const wrapper = setup();
    expect(wrapper.find('FlightCell').length).toBe(1);
  });
});