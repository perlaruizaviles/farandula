import React from "react";
import {Button, Item, Segment, Icon} from "semantic-ui-react";
import {Collapse} from "react-collapse";
import Airleg from "./Airleg";
import AirlegDetail from "./AirlegDetail";
import {titleize} from "inflection";
import * as airlines from "../../data/airlines";
import logo_aeromexico from "../../assets/images/aeromexico.png";
import logo_interjet from "../../assets/images/interjet.png";
import logo_volaris from "../../assets/images/volaris.png";
import logo_vivaAerobus from "../../assets/images/vivaAerobus.png";

class ItineraryElement extends React.Component {
  constructor(props) {
    super(props);
    this.state = {isOpened: 'false'};
  }

  open = () => {
		this.setState({isOpened: (this.state.isOpened === 'true') ? 'false' : 'true'});
  };

	loadLogo = (logo) => {
		return (
			<Item.Image className="logoAirline" 
				size='tiny'
				src={logo} 
			/>
		);
	}

  logoComponent = (airline) => {
    switch (airline) {
      case airlines.AEROMEXICO:
        return(<Item.Image className="logoAirline"  size='tiny' src={logo_aeromexico} />);
    
      case airlines.INTERJET:
        return(<Item.Image className="logoAirline"  size='tiny' src={logo_interjet} />);

      case airlines.VOLARIS:
        return(<Item.Image className="logoAirline"  size='tiny' src={logo_volaris} />);

      case airlines.VAERO:
        return(<Item.Image className="logoAirline"  size='tiny' src={logo_vivaAerobus} />);

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
              {this.logoComponent(airline)}
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