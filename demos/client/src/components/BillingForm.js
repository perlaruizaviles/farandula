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
    { text: 'Enero', value: '1'},
    { text: 'Febrero', value: '2'},
    { text: 'Marzo', value: '3'},
    { text: 'Abril', value: '4'},
    { text: 'Mayo', value: '5'},
    { text: 'Junio', value: '6'},
    { text: 'Julio', value: '7'},
    { text: 'Agosto', value: '8'},
    { text: 'Septiembre', value: '9'},
    { text: 'Octubre', value: '10'},
    { text: 'Noviembre', value: '11'},
    { text: 'Diciembre', value: '12'},
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