import React from "react";
import {Button, Icon} from "semantic-ui-react";
import PropTypes from "prop-types";


const ExchangeButton = ({handleExchange}) => {
  return (
    <Button icon onClick={(e) => handleExchange()}>
      <Icon name='exchange'/>
    </Button>
  );
};

ExchangeButton.propTypes = {
  handleExchange: PropTypes.func.isRequired
};

export default ExchangeButton;