import React from 'react';
import {shallow} from 'enzyme';
import CounterButtons from '../CounterButtons';
import {List, Map} from 'immutable';

describe('Rendering CounterButtons ', () => {

  const props = {
    count:1,
    travelerTypeCountChange: () => {},
    typeTraveler:'adults'
  };

  const tree = shallow(<CounterButtons {...props} />);

  it('Should create an snapshot for CounterButtons', () => {
    expect(tree).toMatchSnapshot();
  });
});