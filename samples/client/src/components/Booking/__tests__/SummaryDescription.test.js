import React from 'react';
import {shallow} from 'enzyme';
import SummaryDescription from '../SummaryDescription';

describe('Rendering SummaryDescription ', () => {

  const tree = shallow(<SummaryDescription />);
  
  it('Should create an snapshot for SummaryDescription', () => {
    expect(tree).toMatchSnapshot();
  });
  
  it('Renders Correct SummaryDescription', () => {
    expect(tree.find('Container').length).toBe(1);
  });
});