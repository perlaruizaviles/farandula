import React from 'react';
import {shallow} from 'enzyme';
import SummaryCard from '../Booking/SummaryCard';

describe('Rendering SummaryCard ', () => {

  const tree = shallow(<SummaryCard />);
  
  it('Should create an snapshot for SummaryCard', () => {
    expect(tree).toMatchSnapshot();
  });
  
  it('Renders Correct SummaryCard', () => {
    expect(tree.find('Card').length).toBe(1);
  });
});