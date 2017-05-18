import React from 'react';
import TravelList from './TravelList';
import TravelSearch from '../containers/TravelSearch';
import {Container} from 'semantic-ui-react';

const TravelResults = () => (
    <Container>
        <TravelSearch/>
        <TravelList/>
    </Container>
)

export default TravelResults;