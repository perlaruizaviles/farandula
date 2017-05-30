import React from 'react';
import {shallow} from 'enzyme';
import SearchForm from '../SearchForm';

describe('Rendering TravelResults ', () => {

  const tree = shallow(<SearchForm />);
  
  it('Should create an snapshot for TravelResults', () => {
    expect(tree).toMatchSnapshot();
  });
});