import React from 'react';
import {Icon} from 'semantic-ui-react';
import moment from 'moment';

const AirlegSummary = ({airleg}) => {
    let departureDate = moment.unix(airleg.departureDate);
    let arrivalDate = moment.unix(airleg.arrivalDate);

    let departureTime = departureDate.format('h:mmp');
    let arrivalTime = arrivalDate.format('h:mmp');
    let totalHours = arrivalDate.diff(departureDate, 'hours');
    let clearMinutes = arrivalDate.diff(departureDate, 'minutes') % 60;
    return(
        <p>
            {departureTime} &emsp; <strong>{airleg.departureAirport.iata}</strong> 
            &emsp;<Icon name="long arrow right"/>&emsp; 
            {arrivalTime} &emsp; <strong>{airleg.arrivalAirport.iata}</strong> &emsp;
            <span style={{color:'gray'}}>{totalHours+"hr "+clearMinutes+"m"}</span>&emsp;
            <span style={{color:'gray'}}>1 stop</span>&emsp;
            <span style={{color:'gray'}}>(MEX, NRT)</span>
        </p>
    );
}

export default AirlegSummary;