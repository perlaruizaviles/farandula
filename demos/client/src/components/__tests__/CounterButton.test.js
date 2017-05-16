import React from 'react';
import {shallow} from 'enzyme';
import expect from 'expect';
import CounterButtons from '../CounterButtons';

function setup() {
  const props = {};
  return shallow(<CounterButtons {...props} />);
}

describe('Rendering CounterButtons ', () => {
  it('Renders ButtonGroup', () => {
    const wrapper = setup();
    expect(wrapper.find('ButtonGroup').length).toBe(1);
  });
});