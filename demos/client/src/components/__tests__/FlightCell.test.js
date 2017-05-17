import React from 'react';
import {shallow} from 'enzyme';
import expect from 'expect';
import FlightCell from '../FlightCell';

function setup() {
    const props = {
        changePriceSection: '999.99',
        firstHour: '7:46',
        secondHour: '9:55',
        firstCity: 'CUU', 
        secondCity: 'MTY'
    }
    return shallow(<FlightCell {...props} />);
}

describe('Rendering FlightCell ', () => {
  it('Renders FlightCell', () => {
    const wrapper = setup();
    expect(wrapper.find('PriceSection').length).toBe(1);
    expect(wrapper.find('FlightSection').length).toBe(1);
  });
});