import React from 'react';
import {Icon} from 'semantic-ui-react';
import moment from 'moment';

const AirlegSummary = ({airleg}) => {
    let departureTime = moment.unix(airleg.departureDate).hour()+":"+moment.unix(airleg.departureDate).minute();
    let arrivalTime = moment.unix(airleg.arrivalDate).hour()+":"+moment.unix(airleg.arrivalDate).minute();
    let diffTimes = airleg.arrivalDate-airleg.departureDate;
    let travelTime = moment(diffTimes).hour()+":"+moment(diffTimes).minute();
    return(
    <p>
        {departureTime} &emsp; <strong>{airleg.departureAirport.iata}</strong> 
        &emsp;<Icon name="long arrow right"/>&emsp; 
        {arrivalTime} &emsp; <strong>{airleg.arrivalAirport.iata}</strong> &emsp;
        <span style={{color:'gray'}}>{travelTime}</span>&emsp;
        <span style={{color:'gray'}}>1 stop</span>&emsp;
        <span style={{color:'gray'}}>(MEX, NRT)</span>
    </p>
);
}

export default AirlegSummary;