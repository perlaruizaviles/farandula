import React from 'react';
import AirportField from './AirportField.js';
import{Grid, Header, Icon} from 'semantic-ui-react';

export default () => (
  <Grid>
    <Grid.Column width={4}>
      <Header>Flying from </Header>
      <AirportField />
    </Grid.Column>
    <Grid.Column width={1}>
      <Icon name='exchange' size='big'/>
    </Grid.Column>
    <Grid.Column width={4}>
      <Header>Flying to </Header>
      <AirportField />
    </Grid.Column>
  </Grid>
);