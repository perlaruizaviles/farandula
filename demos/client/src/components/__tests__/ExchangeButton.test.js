import React from 'react';
import {shallow} from 'enzyme';
import expect from 'expect';
import ExchangeButton from '../ExchangeButton';

function setup() {
  const props = {
    handleExchange: () => {}
  };

  return shallow(<ExchangeButton {...props} />);
}


describe('Rendering ExchangeButton', () => {

  it('renders Button', () => {
    const wrapper = setup();
    expect(wrapper.find('Button').length).toBe(1);
  });

  it('renders correct Icon', () => {
    const wrapper = setup();
    expect(wrapper.find('Icon').length).toBe(1);
    expect(wrapper.find('Icon').props().name).toBe('exchange');
  });

});

