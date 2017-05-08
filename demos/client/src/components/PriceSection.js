import React from 'react'
import { Button, Grid, Header } from 'semantic-ui-react'

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
        <Grid.Column>
          <Button content='View Deal' />
        </Grid.Column>
      </Grid.Row>
    </Grid>
  </div>
)

export default PriceSection


