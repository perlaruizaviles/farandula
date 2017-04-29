import {Map, List} from 'immutable';
import {findOptionById} from './flightOptions';
import flightOptions from '../data/flightOptions';

it('selects correctly from the cabin type', () => {
  expect(findOptionById(flightOptions, 'cabin', 'economy'))
    .toEqual(Map({
      id: 'economy',
      name: 'economy'
    }));

  expect(findOptionById(flightOptions, 'cabin', 'first'))
    .toEqual(Map({
      id: 'first',
      name: 'first'
    }));
});

it('selects correctly from the passengers type', () => {
  expect(findOptionById(flightOptions, 'passengers', 'adult'))
    .toEqual(Map({
      id: 'adult',
      name: 'adult',
      ageRange: List([18, 64])
    }));

  expect(findOptionById(flightOptions, 'passengers', 'seat-infant'))
    .toEqual(Map({
      id: 'seat-infant',
      name: 'seat infant',
      ageRange: List([0, 2])
    }));
});

it('throws an error if the type of option isnt valid', () => {
  expect(() => findOptionById(flightOptions, 'foobar', 'economy')).toThrow();
  expect(() => findOptionById(flightOptions, 'bazquux', 'adult')).toThrow();
});