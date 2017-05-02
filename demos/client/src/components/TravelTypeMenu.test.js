import React from 'react';
import {shallow, mount} from 'enzyme';
import TravelTypeMenu from './TravelTypeMenu';

it('renders without crashing (shallow)', () => {
  shallow(<TravelTypeMenu/>);
});

it('renders without crashing (full)', () => {
  mount(<TravelTypeMenu/>);
});