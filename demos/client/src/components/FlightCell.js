import React from 'react';
import { Segment, Grid, Button } from 'semantic-ui-react';
import PriceSection from './PriceSection';
import FlightSection from './FlightSection';
import {Collapse} from 'react-collapse';
import FlightDetail from './FlightDetail';

class FlightCell extends React.Component {
    constructor(props){
        super(props);
        this.state = {...props, isOpened:'false'};
    }
    open = () => {
        if(this.state.isOpened==='true') this.setState({isOpened:'false'});
        if(this.state.isOpened==='false') this.setState({isOpened:'true'});
    }
    render(){
        return(
            <Segment className="raised">
                <Grid divided={true}>
                    <Grid.Row columns={3}>
                        <Grid.Column>
                            <PriceSection changePriceSection={this.state.changePriceSection}/>
                        </Grid.Column>
                        <Grid.Column style={{textAlign: 'center'}}>
                            <FlightSection firstHour={this.state.firstHour} secondHour={this.state.secondHour} firstCity={this.state.firstCity} secondCity={this.state.secondCity}/>
                        </Grid.Column>
                        <Grid.Column>
                            <Button primary onClick={this.open} content='Show details'></Button>
                        </Grid.Column>
                    </Grid.Row>
                    <Grid.Row >
                        <Collapse style={{width:'100%'}} isOpened={this.state.isOpened==='true'}>
                            <FlightDetail/>
                        </Collapse>
                    </Grid.Row>
                </Grid>
            </Segment>
        )
    }
}

export default FlightCell;
