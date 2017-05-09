import React from 'react';
import {shallow, mount} from 'enzyme';
import Welcome from '../Welcome';


// Shallow smoke rendering test
it('renders without crashing (shallow)', () => {
  shallow(<Welcome/>);
});

// Full smoke rendering test
it('renders without crashing (full)', () => {
  mount(<Welcome/>);
});

it('shows the welcoming header', () => {
  const wrapper = shallow(<Welcome/>);
  const header = <h1>Welcome to Quantum Farandula</h1>;
  expect(wrapper).toContainReact(header);
});

it('shows the work in progress paragraph', () => {
  const wrapper = shallow(<Welcome/>);
  const paragraph = <p>This is a <strong>work in progress</strong>.</p>;
  expect(wrapper).toContainReact(paragraph);
});

it('does\'t mention Marcos', () => {
  const wrapper = shallow(<Welcome/>);
  expect(wrapper).not.toIncludeText('Marcos');
});
