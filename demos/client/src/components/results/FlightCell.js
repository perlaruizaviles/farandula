/**
 * Created by antoniohernandez on 5/1/17.
 */
import React, { Component } from 'react'
import { Segment, Grid, Button } from 'semantic-ui-react'
import PriceSection from './PriceSection'
import FlightInfo from './FlightSection'

const Cell = () => {
    return(
            <Segment className="raised">
                <Grid divided = {true}>
                    <Grid.Row columns={3}>
                        <Grid.Column>
                            <PriceSection/>
                        </Grid.Column>
                        <Grid.Column>
                            <FlightInfo/>
                        </Grid.Column>
                        <Grid.Column style={{textAlign: 'center'}}>
                            <Button basic color='violet' >Mostrar Detalles</Button>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
            </Segment>
    )
}



export default Cell;

