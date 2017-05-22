import React from 'react';
import TravelList from './TravelList';
import OptionsTravel from './OptionsTravel';
import TravelSearch from '../containers/TravelSearch';
import {Container, Message} from 'semantic-ui-react';

const TravelResults = ({travels, order}) => {
    return (
        <Container>
            <OptionsTravel config={order}/>
            <TravelSearch/>
            {
                (travels)? 
                <TravelList travels={travels}/>:
                <Message warning 
                header='You must search flight first to see the list!'
                content=':P'/>
            }
        </Container>
    )
}

export default TravelResults;