import React from 'react';
import {shallow, mount} from 'enzyme';
import DropCabinMenu from '../DropCabinMenu';
import {Map, List} from 'immutable';

function setup(){
    const props = {
        config: Map({
            cabin: 'economy',
            travelers:Map({
                'adults': 1,
                'seniors': 0,
                'youth': 0,
                'child': 0,
                'seat-infant': 0,
                'lap-infant': 0
            })
        }),
        options: Map({cabin: List(['economy', 'premium-economy', 'business', 'first'])}),
        travelerTypeCountChange: () => {},
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