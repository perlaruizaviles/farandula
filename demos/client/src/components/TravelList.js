import React from 'react';
import TravelElement from './TravelElement';
import {Segment} from 'semantic-ui-react';

const TravelList = () => (
    <Segment raised>
        <TravelElement/>
        <TravelElement/>
        <TravelElement/>
        <TravelElement/>
    </Segment>
);

export default TravelList;