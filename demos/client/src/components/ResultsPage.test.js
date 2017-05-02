import React from 'react';
import {shallow, mount} from 'enzyme';
import Results from './ResultsPage';
import Cell from './results/FlightCell';
import PriceSection from './results/PriceSection'
import FlightInfo from './results/FlightSection'

describe('Results PAge tests', () => {
    it('renders without crashing (shallow)', () => {
        const wrapper = shallow(<Cell/>);
    });

    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
        shallow(<Results/>);
    });

    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
        shallow(<PriceSection/>);
    });

    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
        shallow(<FlightInfo/>);
    });
});
