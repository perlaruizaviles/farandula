import React from "react";
import AirlegSegment from "./AirlegSegment";
import {diffFormatted} from "../../util/dates";
import moment from "moment";

const AirlegDetail = ({index, departureDate, arrivalDate, segments}) => {
  let dateStart = moment.unix(segments[0].departureDate).format("dddd, MMMM Do");
  let totalTime = diffFormatted(departureDate, arrivalDate);
  return (
    <div>
      <span className='travelTitle'>Travel {index + 1} &mdash; {dateStart}</span><span
      style={{float: 'right'}}>{totalTime}</span>
      {segments.map((segment) => <AirlegSegment key={Math.random()} {...segment}/>)}
    </div>
  )
};

export default AirlegDetail;