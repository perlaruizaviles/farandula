import React from "react";
import ItineraryElement from "./ItineraryElement";
import ItineraryListOptions from "./ItineraryListOptions";
import {Message, Segment, Item} from "semantic-ui-react";

const ItineraryList = ({travels, order, changeOrderPrice}) => (
  <Segment raised>
    <Item.Group divided>
      <Item>
        <ItineraryListOptions order={order} changeOrder={changeOrderPrice}/>
      </Item>
      {
        (travels)
          ? travels.map((travel) => <ItineraryElement key={Math.random()} itinerary={travel}/>)
          : <Message warning
                    header='You must search flight first to see the list!'
                    content=':P'/>
      }
    </Item.Group>
  </Segment>
);

export default ItineraryList;