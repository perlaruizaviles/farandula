import React from 'react';
import {render} from 'enzyme';
import ItineraryListOptions from '../ItineraryListOptions';

describe('Rendering AirlegDetail ', () => {

  const tree = render(<ItineraryListOptions />);
  
  it('Should create an snapshot for AirlegDetail', () => {
    expect(tree).toMatchSnapshot();
  });
});