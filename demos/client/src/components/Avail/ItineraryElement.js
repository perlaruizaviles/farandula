import React from "react";
import {Button, Item, Segment, Icon} from "semantic-ui-react";
import {Collapse} from "react-collapse";
import Airleg from "./Airleg";
import AirlegDetail from "./AirlegDetail";
import {titleize} from "inflection";
import * as airlines from "../../data/airlines";

class ItineraryElement extends React.Component {
  constructor(props) {
    super(props);
    this.state = {isOpened: 'false'};
  }

  open = () => {
		this.setState({isOpened: (this.state.isOpened === 'true') ? 'false' : 'true'});
  };

	loadLogo = (url) => {
		return (
			<Item.Image className="logoAirline" 
				size='tiny'
				src={url} 
			/>
		);
	}

  logoContent = (airline) => {

		switch (airline) {
			case airlines.AEROMEXICO:
				return (this.loadLogo('https://sprcdn-prod0-sam.sprinklr.com/9009/http___centreforaviation.com_i-c6c4914b-fc50-43d5-938d-58e90c1fe5aa-1949853016.png'));
			case airlines.INTERJET:
				return (this.loadLogo('https://www.seatlink.com/images/logos/no-text/sm/interjet.png'));
			case airlines.VOLARIS:
				return (this.loadLogo('http://www.tijuanazonkeys.com.mx/images/socios/volaris.png'));
			case airlines.VAERO:
				return (this.loadLogo('https://www.seatlink.com/images/logos/vivaaerobus.png'));
			default:
				return (<Item.Image><Icon name='plane' color='orange' size='massive'/></Item.Image>);
		}
  }

  render() {

		const {price, airlegs} = this.props;

		let airline = decodeURIComponent(escape(this.props.airline));

    return (
      <Segment>
        <Item.Group>
          <Item>
              {this.logoContent(airline)}
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