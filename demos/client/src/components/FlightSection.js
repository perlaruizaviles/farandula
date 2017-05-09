import React from 'react'
import { Divider, Grid, Icon } from 'semantic-ui-react'

const FlightSection = ({firstHour, secondHour, firstCity, secondCity}) => (
    <div>
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
        
        <Divider horizontal>Return</Divider>
        <Grid>
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
        </Grid>
    </div>
)

export default FlightSection