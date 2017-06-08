import React from "react";
import {Button, Item, Segment} from "semantic-ui-react";
import {Collapse} from "react-collapse";
import Airleg from "./Airleg";
import AirlegDetail from "./AirlegDetail";
import {titleize} from "inflection";
import logos from "../../data/airlines";

class ItineraryElement extends React.Component {
  constructor(props) {
    super(props);
    this.state = {isOpened: 'false'};
  }

  open = () => {
		this.setState({isOpened: (this.state.isOpened === 'true') ? 'false' : 'true'});
  };

  render() {

		const {price, airlegs} = this.props;

		let airline = decodeURIComponent(escape(this.props.airline));

    console.log(logos.get('Multiple Airlines'));
    console.log('airline: '+airline)
    console.log(logos.get(airline));

    return (
      <Segment>
        <Item.Group>
          <Item>
              <Item.Image className="logoAirline"  size='tiny' src={logos.get(airline)}/>
            <Item.Content>
							<Item.Meta>
								<span className='cinema'>{titleize(airline)}</span>
							</Item.Meta>
              <Item.Description>
                {airlegs.map((airleg) => <Airleg key={Math.random()} {...airleg} />)}
              </Item.Description>
              <Item.Extra><Button size='tiny' compact
               color='blue' onClick={this.open}>View details</Button></Item.Extra>
            </Item.Content>
            <Item.Extra className="itineraryExtraSection" style={{width: '15%'}}>
              <p className="itineraryPrice">USD ${price}</p>
              <center><Button disabled={true} className='orange' content='Book'/></center>
            </Item.Extra>
          </Item>

          <Collapse isOpened={this.state.isOpened === 'true'}>
            {airlegs.map((airleg, index) => <AirlegDetail index={index} key={Math.random()} {...airleg}/>)}
          </Collapse>
        </Item.Group>
      </Segment>
    )
  }
}

export default ItineraryElement;