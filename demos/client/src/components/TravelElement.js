import React from 'react';
import {Item, Segment} from 'semantic-ui-react';
import {Collapse} from 'react-collapse';
import TravelSummary from './TravelSummary';
import TravelDetail from './TravelDetail';
import {List} from 'immutable';

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
            price: this.state.data.fares.total.amount,
            airlegs: List([
                this.state.data.departureAirleg,
                this.state.data.returningAirleg
            ])
        };
        return(
            <Segment>
                <Item.Group divided>
                    <TravelSummary open={this.open} airlegs={travel.airlegs} price={travel.price}/>
                    <Collapse isOpened={this.state.isOpened==='true'}>
                        <TravelDetail/>
                    </Collapse>
                </Item.Group>
            </Segment>
        )
    }
}

export default TravelElement;