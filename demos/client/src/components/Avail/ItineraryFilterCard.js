import React from "react";
import {Card} from "semantic-ui-react";

const OptionsTravel = ({options, config}) => {
  return (
    <Card className='orange'>
      <Card.Content>
        <Card.Header>
          Filters
        </Card.Header>
      </Card.Content>
      <Card.Content>
        Aqui iran los filtros
      </Card.Content>
    </Card>
  )
};

export default OptionsTravel;