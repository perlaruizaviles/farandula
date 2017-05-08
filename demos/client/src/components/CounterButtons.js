import React from 'react';
import { Button } from 'semantic-ui-react';

const CounterButtons = ({count, travelerTypeCountChange, typeTraveler}) => (
    <Button.Group>
        <Button onClick={() => travelerTypeCountChange(typeTraveler, count-1)}> - </Button>
        <Button.Or text={count}/>
        <Button onClick={() => travelerTypeCountChange(typeTraveler, count+1)}> + </Button>
    </Button.Group>
);

export default CounterButtons;