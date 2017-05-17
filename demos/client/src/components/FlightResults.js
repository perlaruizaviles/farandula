import React from 'react';
import FlightCell from './FlightCell';
import travelOptions from '../data/travelOptions';
import TravelSearch from '../containers/TravelSearch';
import {Container} from 'semantic-ui-react';

export default () => (
    <Container>
        <h2>Flight Results</h2>

        <TravelSearch/>

        <FlightCell changePriceSection={travelOptions.get('price').get(0)}
                    firstHour={travelOptions.get('hour').get(0)}
                    secondHour={travelOptions.get('hour').get(1)}  
                    firstCity={travelOptions.get('city').get(0)}    
                    secondCity={travelOptions.get('city').get(1)}/>
    </Container>
)