import React from 'react';
import TravelTypeMenu from './TravelTypeMenu';
import travelOptions from '../data/travelOptions';
import {List} from 'immutable';

export default () => (
  <div>
    <h1>Un menú para el tipo de viaje</h1>
    <TravelTypeMenu options={travelOptions.get('type')}
                    selected="round-trip"
                    selectType={(type) => console.log(`Selected ${type} type`)} />
    <TravelTypeMenu options={List(['Alan', 'Eduardo', 'Luque'])}
                    selected="Alan"
                    selectType={type => console.log(`Hay flow con el ${type}`)} />
  </div>
);