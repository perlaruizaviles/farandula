import {Map} from 'immutable';
import {countPassengers} from './flightSettings';

const validSettings1 = Map({
  cabin: 'economy',
  passengers: Map({
    'adult':       2,
    'senior':      1,
    'youth':       4,
    'child':       1,
    'seat-infant': 6,
    'lap-infant':  1
  })
});

const validSettings2 = Map({
  cabin: 'economy',
  passengers: Map({
    'adult':       2,
    'senior':      1,
    'youth':       2,
    'child':       3,
    'seat-infant': 4,
    'lap-infant':  5
  })
});

const validSettings3 = Map({
  cabin: 'economy',
  passengers: Map({
    'adult':       0,
    'senior':      0,
    'youth':       0,
    'child':       0,
    'seat-infant': 0,
    'lap-infant':  0
  })
});

it('counts the number of passengers', () => {
  expect(countPassengers(validSettings1)).toBe(15);
  expect(countPassengers(validSettings2)).toBe(17);
  expect(countPassengers(validSettings3)).toBe(0);
});

const invalidSettings1 = Map({
  cabin: 'economy',
  passengers: Map({
    'adult':       10,
    'senior':      -5,
    'youth':       -4,
    'child':       0,
    'seat-infant': -3,
    'lap-infant':  0
  })
});

const invalidSettings2 = Map({
  cabin: 'economy',
  passengers: Map({
    'adult':       -1,
    'senior':       0,
    'youth':        0,
    'child':        1,
    'seat-infant': -1,
    'lap-infant':   0
  })
});

it('throws an error when the count is negative', () => {
  expect(() => countPassengers(invalidSettings1)).toThrow();
  expect(() => countPassengers(invalidSettings2)).toThrow();
});