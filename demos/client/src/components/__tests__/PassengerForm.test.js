import React from 'react';
import {shallow} from 'enzyme';
import PassengerForm from '../Booking/PassengerForm';

describe('Rendering PassengerForm ', () => {

  const props = {
    name: 'Jhonatan Laguna'
  };

  const tree = shallow(<PassengerForm {...props}/>);
  
  it('Should create an snapshot for PassengerForm', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders Correct PassengerForm', () => {
    expect(tree.find('FormSection').length).toBe(1);
  });
});