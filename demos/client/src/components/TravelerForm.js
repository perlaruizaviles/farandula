import React from "react";
import {Divider, Form} from "semantic-ui-react";
import tralvelOptions from "../data/travelOptions";
import DateSelector from "./DateSelector";


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
                <Form.Input placeholder='Phone number*'/>
            </Form.Group>
            
            <Divider />
            <Form.Group widths='equal'>
              <DateSelector/>
              <Form.Select placeholder='Gender*' options={tralvelOptions.get('genders')} width={2}/>
            </Form.Group>
        </Form>
    </div>
);

export default TravelerForm;