import React from 'react';
import {shallow} from 'enzyme';
import ExchangeButton from '../Common/ExchangeButton';

describe('Rendering ExchangeButton ', () => {

  const props = {
    handleExchange: () => {}
  };

  const tree = shallow(<ExchangeButton {...props} />);

  it('Should create an snapshot for ExchangeButton', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders Button', () => {
    expect(tree.find('Button').length).toBe(1);
  });

  it('Renders Correct Icon', () => {
    expect(tree.find('Icon').length).toBe(1);
  });

  it('Renders Correct exchange Icon', () => {
    expect(tree.find('Icon').props().name).toBe('exchange');
  });
});