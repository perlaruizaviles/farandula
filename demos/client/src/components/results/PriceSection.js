/**
 * Created by antoniohernandez on 5/1/17.
 */

import React, { Component } from 'react'
import { Button, Grid } from 'semantic-ui-react'
const PriceSection = () => {
    return(

        <Grid style={{textAlign: 'center'}}>

            <Grid.Row columns={1}>
                <Grid.Column>
                    Precio
                </Grid.Column>
            </Grid.Row>
            <Grid.Row columns={1}>
                <Grid.Column>
                    <Button basic color='olive'>Ver Vuelo</Button>
                </Grid.Column>
            </Grid.Row>

        </Grid>
    )

}
export default PriceSection;