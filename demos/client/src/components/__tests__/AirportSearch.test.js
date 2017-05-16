import React from 'react';
import {shallow} from 'enzyme';
import expect from 'expect';
import AirportSearch from '../AirportSearch';
import {List} from 'immutable';

function setup() {
  const props = {
    results: List['x'], value: 'x',
    onSearchChange: () => {},
    onResultSelect: () => {}
  };
  return shallow(<AirportSearch {...props} />);
}

describe('Rendering AirportSearch', () => {
  it('Renders Search', () => {
    const wrapper = setup();
    expect(wrapper.find('Search').length).toBe(1);
  });

  it('Initial Value ', () => {
    const wrapper = setup();
    expect(wrapper.find('Search').props().value).toBe('x');
  });
});