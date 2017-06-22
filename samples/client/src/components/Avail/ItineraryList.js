import React from "react";
import ItineraryElement from "./ItineraryElement";
import ItineraryListOptions from "./ItineraryListOptions";
import {Item, Message, Segment, Dimmer, Loader} from "semantic-ui-react";
import {airlineNameByAirlegs} from "../../util/itinerary";

class ItineraryList extends React.Component {
  render() {

    const {travels, order, changeOrderPrice, loading} = this.props;

    const listHeaderContent = (travels) => {
      if(travels){
        if (travels.size > 0){
          return  <ItineraryListOptions order={order} changeOrder={changeOrderPrice}/>;
        } else {
          return (
            <Message warning
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
    };

    return (
      <Segment raised color="orange">

        <Dimmer active={loading} inverted>
          <Loader content='Loading'/>
        </Dimmer>

        <Item.Group divided>
          <Item>
            {
              listHeaderContent(travels)
            }
            
          </Item>
          {
            (travels)
              ? travels.map((travel) => {
                let airline = airlineNameByAirlegs(travel.airlegs);
                return (
                  <ItineraryElement key={Math.random()} 
                    itinerary={travel}
										price={travel.fares.basePrice.amount}
										airlegs={travel.airlegs}
                    airline={airline}/>
                )})
              : ""
          }
        </Item.Group>
      </Segment>
    )
  }
}

export default ItineraryList;