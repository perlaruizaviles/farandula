import React from 'react';
import {shallow} from 'enzyme';
import ItineraryList from '../ItineraryList';

describe('Rendering AirlegDetail ', () => {

  const tree = shallow(<ItineraryList />);
  
  it('Should create an snapshot for AirlegDetail', () => {
    expect(tree).toMatchSnapshot();
  });
});