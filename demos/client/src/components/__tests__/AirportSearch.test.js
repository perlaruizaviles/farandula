import React from 'react';
import {shallow} from 'enzyme';
import AirportSearch from '../Avail/AirportSearch';

describe('Rendering AirportSearch ', () => {

  const props = {
    changeSelected: () => {},
    searchChange: () => {},
    airports: [{title: 'Mexicali - MXL', description: 'General Rodolfo Se1nchez Taboada International Airport'}],
    value: "Mexicali - MXL",
    cleanField: () => {}
  };

  const tree = shallow(<AirportSearch {...props} />);

  it('Should create an snapshot for AirportSearch', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders Search', () => {
    expect(tree.find('Search').length).toBe(1);
  });

  it('Initial Value for AirportSearch', () => {
    expect(tree.find('Search').props().value).toBe('Mexicali - MXL');
  });
});