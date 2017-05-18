import React from "react";
import {Divider, Form} from "semantic-ui-react";
import tralvelOptions from "../data/travelOptions";


const BillingForm = () => (
    <div>
        <h2>Billing Information</h2>
        <Form>
            <Form.Group widths='equal'>
                <Form.Input placeholder='Street (line 1)*'/>
                <Form.Input placeholder='Street (line 2)'/>
            </Form.Group>
            <Form.Group widths='equal'>
                <Form.Input placeholder='Postal Code*'/>
                <Form.Input placeholder='City*'/>
            </Form.Group>
            <Form.Group widths='equal'>
                <Form.Select placeholder='State/Region' options={tralvelOptions.get('states')} />
            </Form.Group>
            
            <Divider />

            <h2>Card Details</h2>
            <Form.Group widths='equal'>
                <Form.Input placeholder='Name On Card*'/>
                <Form.Input placeholder='Credit Card Number*'/>
            </Form.Group>
            <Form.Group widths='equal'>
                <Form.Select placeholder='Month*' options={tralvelOptions.get('months')}/>
                <Form.Select placeholder='Year*' options={tralvelOptions.get('years')}/>
                <Form.Input placeholder='Security Code*'/>
            </Form.Group>
        </Form>
    </div>
);

export default BillingForm;