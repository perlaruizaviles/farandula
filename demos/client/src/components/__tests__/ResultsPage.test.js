import React from 'react';
import {shallow} from 'enzyme';
import TravelResults from '../Avail/ResultsPage';

describe('Rendering TravelResults ', () => {

  const tree = shallow(<TravelResults />);
  
  it('Should create an snapshot for TravelResults', () => {
    expect(tree).toMatchSnapshot();
  });
  
  it('Renders Correct TravelResults', () => {
    expect(tree.find('Container').length).toBe(1);
  });
});