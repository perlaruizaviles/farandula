import React from 'react';
import {shallow, mount} from 'enzyme';
import TextMenu from '../TextMenu';
import {Map, List} from 'immutable';

it('renders without crashing (shallow)', () => {
  shallow(<TextMenu options={List(['x'])} selected={'x'} />);
});

it('renders without crashing (full)', () => {
  mount(<TextMenu options={List(['x'])} selected={'x'} />);
});