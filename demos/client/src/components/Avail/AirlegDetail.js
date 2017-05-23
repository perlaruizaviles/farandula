import React from "react";
import AirlegSegment from "./AirlegSegment";
import {diffFormatted} from "../../util/dates";
import moment from "moment";

const AirlegDetail = ({departureDate, arrivalDate, departureAirport, arrivalAirport, segments}) => {
  let dateStart = moment(segments[0].departureDate).format("dddd, MMMM Do");
  let totalTime = diffFormatted(departureDate, arrivalDate);
  return (
    <div>
      <strong><span style={{background: 'lightgray'}}>Travel</span> &mdash; {dateStart}</strong><span
      style={{float: 'right'}}>{totalTime}</span>
      {segments.map((segment) => <AirlegSegment key={Math.random()} {...segment}/>)}
    </div>
  )
};

export default AirlegDetail;