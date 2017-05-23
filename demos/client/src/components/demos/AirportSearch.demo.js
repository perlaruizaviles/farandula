import React from 'react';
import AirportSearch from '../Avail/AirportSearch';

export default () => (
  <div>
    <h1>Busqueda de aeropuertos desde el api</h1>
    <AirportSearch
      searchChange={(query, quantum = config.getIn(['locations','to'])) => searchAirport(query,quantum)}
      changeSelected={value => fromAirportChange(value)}
      airports={config.get('airports')}
      value="Hermosillo - HMO"/>
  </div>
);