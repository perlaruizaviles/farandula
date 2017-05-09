import React from 'react';
import TextMenu from './TextMenu';
import DateSelector from './DateSelector';
import AirportSearch from './AirportSearch';
import ExchangeButton from './ExchangeButton';
import travelOptions from '../data/travelOptions';
import DropTravelMenu from './DropTravelMenu';

const TravelSearch = ({config, typeChange, dateChange, travelerTypeCountChange, cabinChange ,searchAirport, fromAirportChange, toAirportChange, exchangeDestinations}) => (
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

    <AirportSearch
        searchChange={(query, quantum = config.getIn(['locations','to'])) => searchAirport(query,quantum)}
        changeSelected={value => fromAirportChange(value)}
        airports={config.get('airports')}
        value={config.getIn(['locations','from']).title}/>

    <ExchangeButton handleExchange={
      (from = config.getIn(['locations','from']), to = config.getIn(['locations','to'])) => exchangeDestinations(from,to)} />

    <AirportSearch
        searchChange={(query, quantum = config.getIn(['locations','from'])) => searchAirport(query,quantum)}
        changeSelected={value => toAirportChange(value)}
        airports={config.get('airports')}
        value={config.getIn(['locations','to']).title}/>
  </div>
);

export default TravelSearch;
