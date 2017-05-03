import React from 'react';
import TextMenu from './TextMenu';
import DateSelector from './DateSelector';
import travelOptions from '../data/travelOptions';

const TravelSearch = ({config, typeChange, changeDateDepart, changeDateReturn}) => (
  <div>
    <TextMenu options={travelOptions.get('type')}
              selected={config.get('type')}
              selectType={typeChange} />
    <DateSelector
    minDate={travelOptions.get('minDate')}
    maxDate={travelOptions.get('maxDate')}
    startDate={config.getIn(['dates', 'depart'])}
    selected={config.getIn(['dates', 'depart'])}
    changeTravelDate={changeDateDepart}
     />
    <DateSelector
    minDate={travelOptions.get('minDate')}
    maxDate={travelOptions.get('maxDate')}
    startDate={config.getIn(['dates', 'return'])}
    selected={config.getIn(['dates', 'return'])}
    changeTravelDate={changeDateReturn}
    />
  </div>
);

export default TravelSearch;
