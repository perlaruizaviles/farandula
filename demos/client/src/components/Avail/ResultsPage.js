import React from "react";
import ItineraryList from "../../containers/ItineraryList";
import TravelSearch from "../../containers/TravelSearch";
import FiltersCard from "../../containers/FiltersCard";
import {Container, Grid} from "semantic-ui-react";

const TravelResults = ({filters}) => {
  return (
    <Container>
      <Grid>
        <Grid.Column width={13} floated='right'>
          <Grid.Row>
            <TravelSearch/>
          </Grid.Row>
          <Grid.Row>
            <ItineraryList />
          </Grid.Row>
        </Grid.Column>
        <Grid.Column width={3} floated='left'>
          <FiltersCard/>
        </Grid.Column>
      </Grid>
    </Container>
  )
};

export default TravelResults;