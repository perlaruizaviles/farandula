import React from 'react';
import {shallow} from 'enzyme';
import BillingForm from '../BillingForm';

describe('Rendering BillingForm ', () => {

  const tree = shallow(<BillingForm />);
  
  it('Should create an snapshot for BillingForm', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders Correct BillingForm', () => {
    expect(tree.find('FormSection').length).toBe(1);
  });
});