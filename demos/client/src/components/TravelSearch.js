import React from 'react';
import TextMenu from './TextMenu';
import DateSelector from './DateSelector';
import AirportSearch from './AirportSearch';
import travelOptions from '../data/travelOptions';

const TravelSearch = ({config, typeChange, dateChange, searchAirport, fromAirportChange, toAirportChange}) => (
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
        searchChange={(query, quantum = config.getIn(['locations','to'])) => searchAirport(query,quantum)}
        changeSelected={value => fromAirportChange(value)}
        airports={config.get('airports')} />

    <AirportSearch
        searchChange={(query, quantum = config.getIn(['locations','from'])) => searchAirport(query,quantum)}
        changeSelected={value => toAirportChange(value)}
        airports={config.get('airports')} />

  </div>
);

export default TravelSearch;
