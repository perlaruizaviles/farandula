import React from 'react';
import TravelSearch from '../containers/TravelSearch';
import FlightList from '../containers/FlightList';
import {Container} from 'semantic-ui-react';

export default () => (
    <Container>
        <h2>Flight Results</h2>
        
        <TravelSearch/>
        
        <FlightList/>
        
    </Container>
)