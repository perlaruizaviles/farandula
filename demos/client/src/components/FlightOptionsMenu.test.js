import React from 'react';
import {shallow, mount} from 'enzyme';
import {Map, List} from 'immutable';
import FlightOptions, {settingsString, ageRangeString} from './FlightOptionsMenu';
import flightOptions from '../data/flightOptions';

const settings1 = Map({
  cabin: 'economy',
  passengers: Map({
    'adult':       1,
    'senior':      0,
    'youth':       0,
    'child':       0,
    'seat-infant': 0,
    'lap-infant':  0
  })
});

const settings2 = Map({
  cabin: 'business',
  passengers: Map({
    'adult':       4,
    'senior':      0,
    'youth':       0,
    'child':       0,
    'seat-infant': 0,
    'lap-infant':  0
  })
});

const settings3 = Map({
  cabin: 'first',
  passengers: Map({
    'adult':       0,
    'senior':      0,
    'youth':       0,
    'child':       5,
    'seat-infant': 0,
    'lap-infant':  0
  })
});

const settings4 = Map({
  cabin: 'premium-economy',
  passengers: Map({
    'adult':       0,
    'senior':      0,
    'youth':       0,
    'child':       0,
    'seat-infant': 0,
    'lap-infant':  9
  })
});

const settings5 = Map({
  cabin: 'economy',
  passengers: Map({
    'adult':       0,
    'senior':      1,
    'youth':       1,
    'child':       1,
    'seat-infant': 0,
    'lap-infant':  0
  })
});

it('unparses settings to strings correctly', () => {
  expect(settingsString(settings1, flightOptions))
    .toEqual('1 adult, Economy');

  expect(settingsString(settings2, flightOptions))
    .toEqual('4 adults, Business');

  expect(settingsString(settings3, flightOptions))
    .toEqual('5 children, First');

  expect(settingsString(settings4, flightOptions))
    .toEqual('9 lap infants, Premium Economy');

  expect(settingsString(settings5, flightOptions))
    .toEqual('3 travelers, Economy');
});

it('unparses age ranges to strings correctly', () => {
  expect(ageRangeString(List([0, 10])))
    .toEqual('under 10');
  expect(ageRangeString(List([0, 33])))
    .toEqual('under 33');
  expect(ageRangeString(List([5, 10])))
    .toEqual('5 - 10');
  expect(ageRangeString(List([9, Infinity])))
    .toEqual('9 -');
});

// Shallow smoke rendering test
it('renders without crashing (shallow)', () => {
  shallow(<FlightOptions settings={settings1}
                         options={flightOptions}
  />);
});

// Full smoke rendering test
it('renders without crashing (full)', () => {
  mount(<FlightOptions settings={settings4}
                       options={flightOptions}
  />);
});