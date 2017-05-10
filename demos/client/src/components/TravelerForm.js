import React from 'react';
import {Form, Divider} from 'semantic-ui-react';

const ladas = [
    { text: '+1', value:'1'},
    { text: '+2', value:'2'},
    { text: '+3', value:'3'},
    { text: '+4', value:'4'},
    { text: '+5', value:'5'}
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

const days = [  {text:'01', value:'1'},
                {text:'02', value:'2'},
                {text:'03', value:'3'},
                {text:'04', value:'4'},
                {text:'05', value:'5'},
                {text:'06', value:'6'},
                {text:'07', value:'7'},
                {text:'08', value:'8'},
                {text:'09', value:'9'},
                {text:'10', value:'10'},
                {text:'11', value:'11'},
                {text:'12', value:'12'}
            ];

const years = [
    {text:'1990', value:'1'},
    {text:'1991', value:'2'},
    {text:'1992', value:'3'}
];

const genders = [
    {text:'Male', value:'1'},
    {text:'Female', value:'2'}
];

const TravelerForm = () => (
    <div>
         <strong>Traveler</strong> - Adult
        <Form>
            <Form.Group widths='equal'>
                <Form.Input placeholder='First Name*'/>
                <Form.Input placeholder='Middle Name*'/>
            </Form.Group>
            <Form.Input placeholder='Last Name*' width={8}/>
            <Form.Group widths='equal'>
                <Form.Input placeholder='Email address*'/>
                <Form.Select placeholder='lada*' options={ladas}/>
                <Form.Input placeholder='Phone number*'/>
            </Form.Group>
            
            <Divider />
            <Form.Group widths='equal'>
                <Form.Select placeholder='Month*' style={{marginRight:20}} options={months}/>
                <Form.Select placeholder='Day*' options={days}/>
                <Form.Select placeholder='Year*' options={years}/>
            </Form.Group>
            <Form.Select placeholder='Gender*' options={genders} width={2}/>
        </Form>
    </div>
)

export default TravelerForm;