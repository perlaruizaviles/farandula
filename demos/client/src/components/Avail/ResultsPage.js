import React from "react";
import ItineraryList from "../../containers/ItineraryList";
import TravelSearch from "../../containers/TravelSearch";
import ItineraryFilterCard from "./ItineraryFilterCard";
import {Container, Grid} from "semantic-ui-react";

const TravelResults = () => {
  return (
    <Container>
      <TravelSearch/>
      <Grid>
        <Grid.Row>
          <Grid.Column width={3}>
            <ItineraryFilterCard/>
          </Grid.Column>
          <Grid.Column width={13}>
            <ItineraryList />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    </Container>
  )
};

export default TravelResults;