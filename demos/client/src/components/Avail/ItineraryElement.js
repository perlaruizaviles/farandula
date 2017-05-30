import React from "react";
import {Button, Item, Segment} from "semantic-ui-react";
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
      price: this.state.itinerary.fares.basePrice.amount,
      airlegs: this.state.itinerary.airlegs
    };

    return (
      <Segment>
        <Item.Group>
          <Item>
            <Item.Image size='tiny'
                        src='https://www.global-benefits-vision.com/wp-content/uploads/2016/02/Plane-Icon.jpg'/>
            <Item.Content>
              <Item.Description>
                {travel.airlegs.map((airleg) => <Airleg key={Math.random()} {...airleg} />)}
              </Item.Description>
              <Item.Extra><Button size='tiny' compact
               color='blue' onClick={this.open}>View details</Button></Item.Extra>
            </Item.Content>
            <Item.Extra style={{width: '15%'}}>
              <h3>USD ${travel.price}</h3>
              <center><Button disabled={true} className='orange' content='Book'/></center>
            </Item.Extra>
          </Item>

          <Collapse isOpened={this.state.isOpened === 'true'}>
            {travel.airlegs.map((airleg, index) => <AirlegDetail index={index} key={Math.random()} {...airleg}/>)}
          </Collapse>
        </Item.Group>
      </Segment>
    )
  }
}

export default ItineraryElement;