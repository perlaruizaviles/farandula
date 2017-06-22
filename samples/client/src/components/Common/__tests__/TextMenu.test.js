import React from 'react';
import {render} from 'enzyme';
import TextMenu from '../TextMenu';
import {List} from 'immutable';

describe('Rendering TextMenu ', () => {
  const props = {
    options: List(['roundTrip', 'oneWay']),
    selected: 'roundTrip',
    selectType: () => {
    }
  };

  const tree = render(<TextMenu {...props} />);

  it('Should create an snapshot for TextMenu', () => {
    expect(tree).toMatchSnapshot();
  });
});