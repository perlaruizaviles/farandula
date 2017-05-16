import React from 'react';
import {shallow, mount} from 'enzyme';
import DropCabinMenu from '../DropCabinMenu';
import {Map, List} from 'immutable';

function setup(){
    const props = {
        config: Map({
            cabin: 'economy'
        }),
        options: Map({cabin: List(['economy', 'premium-economy', 'business', 'first'])}),
        cabinChange: () => {}
    }
    return shallow(<DropCabinMenu {...props} />);
}

describe('Rendering DropCabinMenu ', () => {
  it('Renders Dropdown', () => {
    const wrapper = setup();
    expect(wrapper.find('Dropdown').length).toBe(1);
  });
});