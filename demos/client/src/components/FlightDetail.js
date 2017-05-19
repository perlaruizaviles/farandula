import React from 'react';
import {Divider} from 'semantic-ui-react';
import moment from 'moment';

const FlightDetail = ({segments}) => {
    let dateStart = moment(segments[0].departureDate).format("dddd, MMMM Do");
    return (
    <div>
        <strong>Travel &mdash; {dateStart}</strong><span style={{float:'right'}}>46h 05m</span>
        {segments.map((segment) => {
            let departureDate = moment.unix(segment.departureDate);
            let arrivalDate = moment.unix(segment.arrivalDate);

            let departureTime = departureDate.format('h:mmp');
            let arrivalTime = arrivalDate.format('h:mmp');
            let totalTime = arrivalDate.diff(departureDate, 'hours')+"hrs "
                +arrivalDate.diff(departureDate, 'minutes') % 60+"m";
            return(
                <div key={Math.random()}>
                    <Divider/>
                        <p>
                            <strong>{departureTime} &mdash; {arrivalTime}</strong><br/><span style={{float:'right'}}>Economy {totalTime}</span>
                            {segment.departureAirport.city} 
                            ({segment.departureAirport.iata})
                            &mdash;
                            {segment.arrivalAirport.city} 
                            ({segment.arrivalAirport.iata})<br/>
                            Aeromexico 2817 - Narrow-body jet<br/>
                        </p>
                    <Divider/>
                </div>
            );
        })}
    </div>
)};

export default FlightDetail;