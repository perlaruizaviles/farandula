import React from 'react';
import TextMenu from './TextMenu';
import DateSelector from './DateSelector';

import travelOptions from '../data/travelOptions';

const TravelSearch = ({config, typeChange, travelDateChange}) => (
  <div>
    <TextMenu options={travelOptions.get('type')}
              selected={config.get('type')}
              selectType={typeChange} />
    <DateSelector
    minDate={travelOptions.get('minDate')}
    maxDate={travelOptions.get('maxDate')}
    startDate={config.getIn(['dates', 'depart'])}
    selected={config.getIn(['dates', 'depart'])}
    changeTravelDate={travelDateChange}
     />
  </div>
);

export default TravelSearch;
