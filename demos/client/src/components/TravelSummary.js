import React from 'react';
import AirlegSummary from './AirlegSummary';
import {Button, Label, Item} from 'semantic-ui-react';

const TravelSummary = ({open, price, airlegs}) => (
    <Item>
        <Item.Image size='tiny' src='https://www.global-benefits-vision.com/wp-content/uploads/2016/02/Plane-Icon.jpg' />
        <Item.Content>
            <Item.Meta>
                <span className='cinema'>Multiple Airlines</span>
            </Item.Meta>
            <Item.Description>

                {airlegs.map((airleg)=> <AirlegSummary key={Math.random()} {...airleg}/>)}

            </Item.Description>
            <Item.Extra><Label color='blue' href="#" onClick={open}>View details</Label></Item.Extra>
        </Item.Content>
        <Item.Extra style={{width:'15%'}}>
            <h2>${price}</h2>
            <Button className='orange' content='View deal'/>
        </Item.Extra>
    </Item>
)

export default TravelSummary;