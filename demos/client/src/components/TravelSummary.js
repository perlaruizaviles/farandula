import React from 'react';
import AirlegSummary from './AirlegSummary';
import {Button, Label, Item} from 'semantic-ui-react';

const TravelSummary = ({open, departureIata, arrivalIata, }) => (
    <Item>
        <Item.Image src='https://react.semantic-ui.com/assets/images/wireframe/image.png' />
        <Item.Content>
            <Item.Meta>
                <span className='cinema'>Multiple Airlines</span>
            </Item.Meta>
            <Item.Description>
                
                <AirlegSummary/>
                <AirlegSummary/>

            </Item.Description>
            <Item.Extra><Label color='blue' href="#" onClick={open}>View details</Label></Item.Extra>
        </Item.Content>
        <Item.Extra style={{width:'15%'}}>
            <h2>$85146</h2>
            <Button className='orange' content='View deal'/>
        </Item.Extra>
    </Item>
)

export default TravelSummary;