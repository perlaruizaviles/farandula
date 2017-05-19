import React from 'react';
import {Item, Segment} from 'semantic-ui-react';
import {Collapse} from 'react-collapse';
import TravelSummary from './TravelSummary';
import TravelDetail from './TravelDetail';

class TravelElement extends React.Component {
    constructor(props){
        super(props);
        this.state = {...props, isOpened:'false'};
    }

    open = () => {
        if(this.state.isOpened==='true') this.setState({isOpened:'false'});
        if(this.state.isOpened==='false') this.setState({isOpened:'true'});
    }
    
    render(){
        let travel = {
            price: this.state.data.fares.totalPrice.amount,
            airlegs: this.state.data.airlegs
        };

        return(
            <Segment>
                <Item.Group divided>
                    <TravelSummary open={this.open} airlegs={travel.airlegs} price={travel.price}/>
                    <Collapse isOpened={this.state.isOpened==='true'}>
                        <TravelDetail airlegs={travel.airlegs}/>
                    </Collapse>
                </Item.Group>
            </Segment>
        )
    }
}

export default TravelElement;