import React from 'react';
import {shallow, mount} from 'enzyme';
import DropTravelMenu from '../DropTravelMenu';

describe('Flight Results Page', () => {
    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
    shallow(<DropTravelMenu/>);
    });

    // Full smoke rendering test
    it('renders without crashing (full)', () => {
    mount(<DropTravelMenu/>);
    });
});