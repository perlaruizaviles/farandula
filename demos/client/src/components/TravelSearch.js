import React from 'react';
import TextMenu from './TextMenu';
import DateSelector from './DateSelector';
import FlightCell from './FlightCell';
import travelOptions from '../data/travelOptions';

const TravelSearch = ({config, typeChange, dateChange}) => (
  <div>
    <TextMenu options={travelOptions.get('type')}
              selected={config.get('type')}
              selectType={typeChange} />

    <DateSelector minDate={travelOptions.get('minDate')}
                  selectsStart
                  maxDate={travelOptions.get('maxDate')}
                  startDate={config.getIn(['dates', 'depart'])}
                  endDate={config.getIn(['dates','return'])}
                  selected={config.getIn(['dates', 'depart'])}
                  changeTravelDate={date => dateChange('depart', date)} />

    <DateSelector minDate={travelOptions.get('minDate')}
                  selectsEnd
                  maxDate={travelOptions.get('maxDate')}
                  startDate={config.getIn(['dates', 'depart'])}
                  endDate={config.getIn(['dates', 'return'])}
                  selected={config.getIn(['dates', 'return'])}
                  changeTravelDate={date => dateChange('return', date)} />
        
    <FlightCell changePriceSection={travelOptions.get('price').get(1)}
                firstHour={travelOptions.get('hour').get(0)}
                secondHour={travelOptions.get('hour').get(1)}  
                firstCity={travelOptions.get('city').get(0)}    
                secondCity={travelOptions.get('city').get(1)}/>

  </div>
);

export default TravelSearch;
