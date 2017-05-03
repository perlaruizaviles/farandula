import React from 'react';
import TextMenu from './TextMenu';

import travelOptions from '../data/travelOptions';

const TravelSearch = ({config, typeChange}) => (
  <div>
    <TextMenu options={travelOptions.get('type')}
              selected={config.get('type')}
              selectType={typeChange} />
  </div>
);

export default TravelSearch;