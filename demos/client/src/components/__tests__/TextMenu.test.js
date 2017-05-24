import React from 'react';
import {render} from 'enzyme';
import TextMenu from '../Common/TextMenu';
import {List} from 'immutable';

describe('Rendering TextMenu ', () => {
  const props = {
    options: List(['round', 'oneWay']),
    selected: 'round',
    selectType: () => {
    }
  };

  const tree = render(<TextMenu {...props} />);

  it('Should create an snapshot for TextMenu', () => {
    expect(tree).toMatchSnapshot();
  });
});