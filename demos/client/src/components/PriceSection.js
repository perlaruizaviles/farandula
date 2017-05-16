import React from 'react';
import { Button, Grid, Header } from 'semantic-ui-react';
import PropTypes from 'prop-types';

const PriceSection = ({changePriceSection}) => (
  <div>
    <Grid style={{textAlign: 'left'}}>
      <Grid.Row columns={1}>
        <Grid.Column>
          <Header sub>Price</Header>
          <span>$ {changePriceSection}</span>
        </Grid.Column>
      </Grid.Row>
      <Grid.Row columns={1}>
        <Grid.Column textAlign='center'>
          <Button className='orange' content='View Deal'/>
        </Grid.Column>
      </Grid.Row>
    </Grid>
  </div>
);

PriceSection.propTypes = {
  changePriceSection: PropTypes.string.isRequired
};

export default PriceSection