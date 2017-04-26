import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<App />, div);
});

test('multiplies some numbers', () => {
  expect(2*3).toEqual(6);
  expect(3*3).toEqual(8);
});