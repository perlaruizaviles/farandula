import React from 'react';
import TravelList from './TravelList';
import TravelSearch from '../containers/TravelSearch';
import {Container} from 'semantic-ui-react';

const TravelResults = ({travels}) => {
    let content = (travels)? <TravelList travels={travels}/>:undefined;
    return (
        <Container>
            <TravelSearch/>
            {content}
        </Container>
    )
}

export default TravelResults;