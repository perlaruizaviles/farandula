import React from "react";
import logo from "../../styles/logo.svg";
import ItineraryElement from "./ItineraryElement";
import ItineraryListOptions from "./ItineraryListOptions";
import {Grid, Item, Segment} from "semantic-ui-react";

const ItineraryList = ({travels, order, changeOrderPrice}) => (
  <Segment raised color="orange">
    <Item.Group divided>
      <Item>
        <ItineraryListOptions order={order} changeOrder={changeOrderPrice}/>
      </Item>
      {
        (travels)
          ? travels.map((travel) => <ItineraryElement key={Math.random()} itinerary={travel}/>)
          : <Grid centered textAlign='center'>
              <Grid.Row>
                <img src={logo} className="App-logo" alt="logo"/>
              </Grid.Row>
            </Grid>
      }
    </Item.Group>
  </Segment>
);

export default ItineraryList;