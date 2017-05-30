import React from 'react';
import {shallow} from 'enzyme';
import Summary from '../Booking';

describe('Rendering Summary ', () => {

  const tree = shallow(<Summary />);
  
  it('Should create an snapshot for Summary', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders Correct Summary', () => {
    expect(tree.find('Grid').length).toBe(1);
  });
});