import React from "react";
import ItineraryElement from "./ItineraryElement";
import ItineraryListOptions from "./ItineraryListOptions";
import {Item, Message, Segment} from "semantic-ui-react";

const ItineraryList = ({travels, order, changeOrderPrice}) => (
  <Segment raised color="orange">
    <Item.Group divided>
      <Item>
        <ItineraryListOptions order={order} changeOrder={changeOrder}/>
      </Item>
      {
        (travels)
          ? travels.map((travel) => <ItineraryElement key={Math.random()} itinerary={travel}/>)
          : <Message info
                     icon="info"
                     header='You must search flight first to see the list!'
                     content=':P'/>
      }
    </Item.Group>
  </Segment>
);

export default ItineraryList;