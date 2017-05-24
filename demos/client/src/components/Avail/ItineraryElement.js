import React from "react";
import {Button, Item, Label, Segment} from "semantic-ui-react";
import {Collapse} from "react-collapse";
import Airleg from "./Airleg";
import AirlegDetail from "./AirlegDetail";

class ItineraryElement extends React.Component {
  constructor(props) {
    super(props);
    this.state = {...props, isOpened: 'false'};
  }

  open = () => {
    if (this.state.isOpened === 'true') this.setState({isOpened: 'false'});
    if (this.state.isOpened === 'false') this.setState({isOpened: 'true'});
  };

  render() {
    let travel = {
      price: this.state.itinerary.fares.totalPrice.amount,
      airlegs: this.state.itinerary.airlegs
    };

    return (
      <Segment>
        <Item.Group>
          <Item>
            <Item.Image size='tiny'
                        src='https://www.global-benefits-vision.com/wp-content/uploads/2016/02/Plane-Icon.jpg'/>
            <Item.Content>
              <Item.Meta>
                <span className='cinema' style={{background: 'lightgray'}}>Multiple Airlines</span>
              </Item.Meta>
              <Item.Description>

                {travel.airlegs.map((airleg) => <Airleg key={Math.random()} {...airleg}/>)}

              </Item.Description>
              <Item.Extra><Label color='blue' onClick={this.open}>View details</Label></Item.Extra>
            </Item.Content>
            <Item.Extra style={{width: '15%'}}>
              <h2>${travel.price}</h2>
              <Button className='orange' content='Book'/>
            </Item.Extra>
          </Item>

          <Collapse isOpened={this.state.isOpened === 'true'}>
            {travel.airlegs.map((airleg) => <AirlegDetail key={Math.random()} {...airleg}/>)}
          </Collapse>
          </Item.Group>
      </Segment>
    )
  }
}

export default ItineraryElement;