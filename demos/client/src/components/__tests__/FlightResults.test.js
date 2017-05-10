import React from 'react';
import {shallow, mount} from 'enzyme';
import PriceSection from '../PriceSection';
import FlightSection from '../FlightSection';
import DetailSection from '../DetailSection';
import FlightCell from '../FlightCell';
import FlightResults from '../FlightResults';

describe('Flight Results Page', () => {
    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
    shallow(<PriceSection/>);
    });

    // Full smoke rendering test
    it('renders without crashing (full)', () => {
    mount(<PriceSection/>);
    });

    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
    shallow(<FlightSection/>);
    });

    // Full smoke rendering test
    it('renders without crashing (full)', () => {
    mount(<FlightSection/>);
    });

    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
    shallow(<DetailSection/>);
    });

    // Full smoke rendering test
    it('renders without crashing (full)', () => {
    mount(<DetailSection/>);
    });

    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
    shallow(<FlightCell/>);
    });

    // Full smoke rendering test
    it('renders without crashing (full)', () => {
    mount(<FlightCell/>);
    });

    // Shallow smoke rendering test
    it('renders without crashing (shallow)', () => {
    shallow(<FlightResults/>);
    });

    // Full smoke rendering test
    it('renders without crashing (full)', () => {
    mount(<FlightResults/>);
    });
});