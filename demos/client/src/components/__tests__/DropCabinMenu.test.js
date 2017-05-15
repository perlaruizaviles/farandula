import React from 'react';
import {shallow, mount} from 'enzyme';
import DropCabinMenu from '../DropTravelMenu';

describe('Flight Results Page', () => {
    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
    shallow(<DropCabinMenu/>);
    });

    // Full smoke rendering test
    it('renders without crashing (full)', () => {
    mount(<DropCabinMenu/>);
    });
});