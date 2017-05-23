import React from 'react';
import TravelElement from './TravelElement';
import {Segment} from 'semantic-ui-react';

const TravelList = ({travels}) => (
    <Segment raised>
        {travels.map((travel)=>
            <TravelElement key={travel.key} data={travel}/>
        )}
    </Segment>
);

export default TravelList;