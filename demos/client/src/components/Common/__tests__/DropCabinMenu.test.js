import React from 'react';
import {render} from 'enzyme';
import DropCabinMenu from '../DropCabinMenu';
import {List, Map} from 'immutable';

describe('Rendering DropCabinMenu ', () => {

  const props = {
    config: Map({
    cabin: 'economy'
    }),
    options: Map({cabin: List(['economy', 'premium-economy', 'business', 'first'])}),
    cabinChange: () => {}
  };

  const tree = render(<DropCabinMenu {...props} />);

  it('Should create an snapshot for DropCabinMenu', () => {
    expect(tree).toMatchSnapshot();
  });
});