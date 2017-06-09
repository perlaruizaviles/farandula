import React from "react";
import {Divider} from "semantic-ui-react";
import {diffFormatted, formatTime} from "../../util/dates";
import {titleize} from "inflection";

const AirlegSegment = ({departureAirport, departureDate, arrivalAirport, arrivalDate, duration, airplaneData, airLineOperationName, airLineMarketingName, cabinTypes}) => {
  let departureTime = formatTime(departureDate);
  let arrivalTime = formatTime(arrivalDate);
  let totalTime = diffFormatted(departureDate, arrivalDate);

  return (
    <div>
      <Divider/>
      <p>
        <strong>
					{departureTime} &mdash; {arrivalTime}
				</strong><br/>
				<span style={{float: 'right'}}>
					{ cabinTypes.map((cabin) => titleize(cabin)) } {totalTime}
				</span>
        {departureAirport.city} ({departureAirport.iata}) &mdash; {arrivalAirport.city } ({arrivalAirport.iata})<br/>
        {decodeURIComponent(escape(airLineMarketingName))} - {airplaneData}<br/>
        <span className='gray'>
					Operated by {decodeURIComponent(escape(airLineOperationName))}
				</span>
      </p>
      <Divider/>
    </div>
  )
};

export default AirlegSegment;