import React from 'react';
import {shallow, mount} from 'enzyme';
import TextMenu from './TextMenu';

it('renders without crashing (shallow)', () => {
  shallow(<TextMenu/>);
});

it('renders without crashing (full)', () => {
  mount(<TextMenu/>);
});