import React from "react";
import ItineraryElement from "./ItineraryElement";
import {Message, Segment} from "semantic-ui-react";

const ItineraryList = ({travels}) => (
  <Segment raised>
    {
      (travels)
        ? travels.map((travel) => <ItineraryElement key={Math.random()} itinerary={travel}/>)
        : <Message warning
                   header='You must search flight first to see the list!'
                   content=':P'/>
    }
  </Segment>
);

export default ItineraryList;