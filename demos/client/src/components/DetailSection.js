import React from 'react'
import { Button, Grid } from 'semantic-ui-react'

const DetailSection = () => (
  <div>
    <Grid style={{textAlign: 'right'}}>
      <Grid.Row columns={1}>
        <Grid.Column>
          <Button primary content='Show details'></Button>
        </Grid.Column>
      </Grid.Row>
    </Grid>
  </div>
)

export default DetailSection