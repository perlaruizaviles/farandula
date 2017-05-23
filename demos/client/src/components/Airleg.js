import React from 'react';
import {Icon} from 'semantic-ui-react';
import {diffFormatted, formatTime} from '../util/dates';

const Airleg = ({departureDate, arrivalDate, departureAirport, arrivalAirport, segments}) => {
    let departureTime = formatTime(departureDate);
    let arrivalTime = formatTime(arrivalDate);
    let totalTime = diffFormatted(departureDate, arrivalDate);
    return(
        <p>
            {departureTime} &emsp; <strong>{departureAirport.iata}</strong> 
            &emsp;<Icon name="long arrow right"/>&emsp; 
            {arrivalTime} &emsp; <strong>{arrivalAirport.iata}</strong> &emsp;
            <span className='gray'>{totalTime}</span>&emsp;
            <span className='gray'>{segments.length-1} stops</span>&emsp;
            <span className='gray'>({getStopsIata(segments)})</span>
        </p>
    );
}

function getStopsIata(segments){
    var iatas = "";
    segments.forEach((x)=>iatas += "-"+x.arrivalAirport.iata)
    return iatas;
}

export default Airleg;