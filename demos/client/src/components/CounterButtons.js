import React from 'react';
import { Button } from 'semantic-ui-react';

const CounterButtons = ({count, increasePassanger, decreasePassanger, typePassanger}) => (
    <Button.Group>
        <Button onClick={() => decreasePassanger(typePassanger, count)}> - </Button>
        <Button.Or text={count}/>
        <Button onClick={() => increasePassanger(typePassanger, count)}> + </Button>
    </Button.Group>
);

export default CounterButtons;