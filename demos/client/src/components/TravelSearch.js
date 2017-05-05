import React from 'react';
import TextMenu from './TextMenu';
import DateSelector from './DateSelector';
import AirportSearch from './AirportSearch';
import travelOptions from '../data/travelOptions';

const TravelSearch = ({config, typeChange, dateChange, loadAirports}) => (
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
    <AirportSearch
        searchChange={() => loadAirports()}
        airports={travelOptions.get('airports')} />

  </div>
);

export default TravelSearch;
