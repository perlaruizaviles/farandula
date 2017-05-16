import React from 'react';
import {shallow} from 'enzyme';
import expect from 'expect';
import TravelerForm from '../TravelerForm';

function setup() {
  const props = {};
  return shallow(<TravelerForm {...props} />);
}

describe('Rendering TravelerForm ', () => {
  it('Renders Form', () => {
    const wrapper = setup();
    expect(wrapper.find('Form').length).toBe(1);
  });

  it('Initial Inputs', () => {
    const wrapper = setup();
    expect(wrapper.find('FormInput').length).toBe(5);
    expect(wrapper.find('FormSelect').length).toBe(5);
  });
});