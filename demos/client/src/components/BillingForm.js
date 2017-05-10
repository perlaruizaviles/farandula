import React from 'react';
import {Form, Divider} from 'semantic-ui-react';

const states = [
    {text: 'Arizona', value: '1'},
    {text: 'Arkansas', value: '2'},
    {text: 'California', value: '3'}
];

const years = [
    {text:'1990', value:'1'},
    {text:'1991', value:'2'},
    {text:'1992', value:'3'}
];

const months = [
    { text: 'January', value: '1'},
    { text: 'February', value: '2'},
    { text: 'March', value: '3'},
    { text: 'April', value: '4'},
    { text: 'May', value: '5'},
    { text: 'June', value: '6'},
    { text: 'July', value: '7'},
    { text: 'August', value: '8'},
    { text: 'September', value: '9'},
    { text: 'October', value: '10'},
    { text: 'November', value: '11'},
    { text: 'December', value: '12'},
];

const countries = [{text:'United States', value: '1'}];

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
                <Form.Select placeholder='State/Region' options={states} />
                <Form.Select defaultValue='1' options={countries} />
            </Form.Group>
            
            <Divider />

            <h2>Card Details</h2>
            <Form.Group widths='equal'>
                <Form.Input placeholder='Name On Card*'/>
                <Form.Input placeholder='Creadit Card Number*'/>
            </Form.Group>
            <Form.Group widths='equal'>
                <Form.Select placeholder='Month*' options={months}/>
                <Form.Select placeholder='Year*' options={years}/>
                <Form.Input placeholder='Security Code*'/>
            </Form.Group>
        </Form>
    </div>
);

export default BillingForm;