import React from 'react';
import FlightDetail from './FlightDetail';

const TravelDetail = ({airlegs}) => (
    <div>
        {airlegs.map((airleg) => <FlightDetail key={Math.random()} segments={airleg.segments}/>)}
    </div>
)
export default TravelDetail;