import React from "react";
import {Button} from "semantic-ui-react";
import PropTypes from "prop-types";


const CounterButtons = ({count, travelerTypeCountChange, typeTraveler}) => (
  <Button.Group>
    <Button onClick={() => travelerTypeCountChange(typeTraveler, count - 1)}> - </Button>
    <Button.Or text={count}/>
    <Button onClick={() => travelerTypeCountChange(typeTraveler, count + 1)}> + </Button>
  </Button.Group>
);

CounterButtons.propTypes = {
  count: PropTypes.number.isRequired,
  travelerTypeCountChange: PropTypes.func.isRequired,
  typeTraveler: PropTypes.string.isRequired
};

export default CounterButtons;