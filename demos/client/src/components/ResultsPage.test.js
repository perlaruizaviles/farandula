import React from 'react';
import {shallow, mount} from 'enzyme';
import Results from './ResultsPage';
import Cell from './results/FlightCell';

// Shallow smoke rendering test
it('renders without crashing (shallow)', () => {
    shallow(<Results/>);
});
// Shallow smoke rendering test
it('renders without crashing (shallow)', () => {
    shallow(<Cell/>);
});
