import React from "react";
import logo from "../../styles/logo.svg";
import ItineraryElement from "./ItineraryElement";
import ItineraryListOptions from "./ItineraryListOptions";
import {Grid, Item, Segment, Message} from "semantic-ui-react";

class ItineraryList extends React.Component {
  render() {

    const {travels, order, changeOrderPrice} = this.props;

    const listHeaderContent = (travels) => {
      if(travels){
        if (travels.size > 0){
          return  <ItineraryListOptions order={order} changeOrder={changeOrderPrice}/>;
        } else {
          return (
            <Message warning
              warning
              icon="warning sign"
              header="Flights not found"
              content="Try a different search"
            />
          );
        }
      }else{
        return (
          <Message
            warning
            icon="warning sign"
            header="No results to show"
            content="Search haven't been done"
          />
        );
      }
    }

    return (
      <Segment raised color="orange">
        <Item.Group divided>
          <Item>
            {
              listHeaderContent(travels)
            }
            
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
    )
  }
};

export default ItineraryList;