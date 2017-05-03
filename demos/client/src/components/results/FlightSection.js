/**
 * Created by antoniohernandez on 5/1/17.
 */

import React from 'react'
import { Label, Button, Divider, Grid, Icon } from 'semantic-ui-react'

const FlightInfo = () => (
    <div>
        <Label pointing='right'>
            <Grid>
                <Grid.Row columns={5}>
                    <Grid.Column>
                        7:46
                    </Grid.Column>
                    <Grid.Column>
                        CUU
                    </Grid.Column>
                    <Grid.Column>
                        <Icon name="long arrow right"></Icon>
                    </Grid.Column>
                    <Grid.Column>
                        9:55
                    </Grid.Column>
                    <Grid.Column>
                        MTY
                    </Grid.Column>
                </Grid.Row>
            </Grid>
        </Label>
        <Divider horizontal>Return</Divider>
        <Label pointing='right'><Grid>
            <Grid.Row columns={5}>
                <Grid.Column>
                    7:46
                </Grid.Column>
                <Grid.Column>
                    MTY
                </Grid.Column>
                <Grid.Column>
                    <Icon name="long arrow right"></Icon>
                </Grid.Column>
                <Grid.Column>
                    9:55
                </Grid.Column>
                <Grid.Column>
                    CUU
                </Grid.Column>
            </Grid.Row>
        </Grid></Label>
    </div>
)

export default FlightInfo