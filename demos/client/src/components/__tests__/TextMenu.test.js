import React from 'react';
import {shallow, mount} from 'enzyme';
import TextMenu from '../TextMenu';
import {List} from 'immutable';

function setup() {
  const props = {};
  return shallow(<TextMenu {...props} />);
}

describe('Rendering TextMenu ', () => {

  it('Renders Without Crashing (Shallow)', () => {
    shallow(<TextMenu options={List(['x'])} selected={'x'} />);
  });

  it('Renders Without Crashing (Full)', () => {
    mount(<TextMenu options={List(['x'])} selected={'x'} />);
  });
});

