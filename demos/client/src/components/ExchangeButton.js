import React from 'react';
import {Icon, Button} from 'semantic-ui-react';

const ExchangeButton = ({handleExchange}) => {
    return(
      <Button icon onClick={(e) => handleExchange()}>
        <Icon name='exchange'/>
      </Button>
    );
};

export default ExchangeButton;