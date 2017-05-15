import React from 'react';
import {shallow, mount} from 'enzyme';
import DropTravelMenu from '../DropTravelMenu';
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
    return props;
}

describe('Flight Results Page', () => {
    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
        const props = setup();
        shallow(<DropTravelMenu {...props}/>)
    });

    // Full smoke rendering test
    /*it('renders without crashing (full)', () => {
        const props = setup();
        mount(<DropTravelMenu {...props}/>)
    });*/
});