import React from "react";
import ItineraryList from "../../containers/ItineraryList";
import TravelSearch from "../../containers/TravelSearch";
import {Container} from "semantic-ui-react";

const TravelResults = () => {
  return (
    <Container>
      <TravelSearch/>
      <ItineraryList />
    </Container>
  )
};

export default TravelResults;