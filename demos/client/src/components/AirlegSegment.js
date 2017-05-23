import React from 'react';
import {Divider} from 'semantic-ui-react';
import {formatTime, diffFormatted} from '../util/dates';

const AirlegSegment = ({departureAirport, departureDate, arrivalAirport, arrivalDate, duration}) => {
    let departureTime = formatTime(departureDate);
    let arrivalTime = formatTime(arrivalDate);
    let totalTime = diffFormatted(departureDate, arrivalDate);
            
    return (
    <div>
        <Divider/>
            <p>
                <strong>{departureTime} &mdash; {arrivalTime}</strong><br/><span style={{float:'right'}}><span style={{background:'lightgray'}}>Economy</span> {totalTime}</span>
                {departureAirport.city} ({departureAirport.iata})
                &mdash;
                {arrivalAirport.city } ({arrivalAirport.iata})<br/>
                <span style={{background:'lightgray'}}>Aeromexico 2817 - Narrow-body jet</span><br/>
            </p>
        <Divider/>
    </div>
    )
}

export default AirlegSegment;