import React from 'react';
import {shallow} from 'enzyme';
import DropTravelMenu from '../Common/DropTravelMenu';
import {Map, List} from 'immutable';

describe('Rendering DropTravelMenu', () => {

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

    const tree = shallow(<DropTravelMenu {...props} />);

    it('Should create an snapshot for DropTravelMenu', () => {
        expect(tree).toMatchSnapshot();
    });

    it('Renders Dropdown', () => {
        expect(tree.find('Dropdown').length).toBe(1);
    });
});