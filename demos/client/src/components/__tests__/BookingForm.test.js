import React from 'react';
import {shallow} from 'enzyme';
import Booking from '../Booking/BookingForm';

describe('Rendering Booking ', () => {

  const tree = shallow(<Booking />);
  
  it('Should create an snapshot for Booking', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders Correct Booking', () => {
    expect(tree.find('Connect').length).toBe(1);
  });
});