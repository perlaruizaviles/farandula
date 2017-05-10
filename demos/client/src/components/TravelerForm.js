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
                <Form.Input placeholder='Last Name*'/>
            </Form.Group>
            <Form.Group widths='equal'>
                <Form.Input placeholder='Email address*'/>
                <Form.Select placeholder='lada*' options={ladas}/>
            </Form.Group>
            <Form.Input placeholder='Phone number*' width={5}/>
            <Divider />
            <Form.Group widths='equal'>
                <Form.Select placeholder='Month*' options={months}/>
                <Form.Select placeholder='Day*' options={days}/>
                <Form.Select placeholder='Year*' options={years}/>
                <Form.Select placeholder='Gender*' options={genders}/>
            </Form.Group>
        </Form>
    </div>
)

export default TravelerForm;