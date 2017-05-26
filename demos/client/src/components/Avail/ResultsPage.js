import React from "react";
import ItineraryList from "../../containers/ItineraryList";
import TravelSearch from "../../containers/TravelSearch";
import {Container, Grid, Card} from "semantic-ui-react";

const TravelResults = () => {
  return (
    <Container>
      <Grid>
        <Grid.Column width={15} floated='right'>
          <Grid.Row>
            <TravelSearch/>
          </Grid.Row>
          <Grid.Row>
            <ItineraryList />
          </Grid.Row>
        </Grid.Column>
        <Grid.Column width={1} floated='left'>
          <Card className='orange fixed'>
            <Card.Content>
              <Card.Header>
                Filters
              </Card.Header>
            </Card.Content>
            <Card.Content>
              Aqui iran los filtros
            </Card.Content>
          </Card>
        </Grid.Column>
      </Grid>
    </Container>
  )
};

export default TravelResults;