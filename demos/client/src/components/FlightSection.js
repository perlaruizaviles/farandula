import React from 'react'
import { Label, Divider, Grid, Icon } from 'semantic-ui-react'

const FlightSection = ({firstHour, secondHour, firstCity, secondCity}) => (
    <div>
        <Label pointing='right'>
            <Grid>
                <Grid.Row columns={5}>
                    <Grid.Column>
                        {firstHour}
                    </Grid.Column>
                    <Grid.Column>
                        {firstCity}
                    </Grid.Column>
                    <Grid.Column>
                        <Icon name="long arrow right"></Icon>
                    </Grid.Column>
                    <Grid.Column>
                        {secondHour}
                    </Grid.Column>
                    <Grid.Column>
                        {secondCity}
                    </Grid.Column>
                </Grid.Row>
            </Grid>
        </Label>
        
        <Divider horizontal>Return</Divider>
        <Label pointing='right'><Grid>
            <Grid.Row columns={5}>
                <Grid.Column>
                    {secondHour}
                </Grid.Column>
                <Grid.Column>
                    {secondCity}
                </Grid.Column>
                <Grid.Column>
                    <Icon name="long arrow right"></Icon>
                </Grid.Column>
                <Grid.Column>
                    {firstHour}
                </Grid.Column>
                <Grid.Column>
                    {firstCity}
                </Grid.Column>
            </Grid.Row>
        </Grid></Label>
    </div>
)

export default FlightSection