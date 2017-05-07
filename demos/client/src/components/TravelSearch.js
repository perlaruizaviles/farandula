import React from 'react';
import TextMenu from './TextMenu';
import DateSelector from './DateSelector';
import travelOptions from '../data/travelOptions';
import DropTravelMenu from './DropTravelMenu';

const TravelSearch = ({config, typeChange, dateChange, travelerTypeCountChange, cabinChange}) => (
  <div>
    <TextMenu
      options={travelOptions.get('type')}
      selected={config.get('type')}
      selectType={typeChange}/>

    <DateSelector
      minDate={travelOptions.get('minDate')}
      selectsStart
      maxDate={travelOptions.get('maxDate')}
      startDate={config.getIn(['dates', 'depart'])}
      endDate={config.getIn(['dates', 'return'])}
      selected={config.getIn(['dates', 'depart'])}
      changeTravelDate={date => dateChange('depart', date)}/>

    <DateSelector
      minDate={travelOptions.get('minDate')}
      selectsEnd
      maxDate={travelOptions.get('maxDate')}
      startDate={config.getIn(['dates', 'depart'])}
      endDate={config.getIn(['dates', 'return'])}
      selected={config.getIn(['dates', 'return'])}
      changeTravelDate={date => dateChange('return', date)}/>

    <DropTravelMenu   
      config={config} 
      options={travelOptions} 
      travelerTypeCountChange={(travelerType, count) => travelerTypeCountChange(travelerType, count)}
      cabinChange={cabinChange} />
  </div>
);

export default TravelSearch;
