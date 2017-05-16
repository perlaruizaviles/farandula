import React from 'react';
import { Divider, Grid, Icon } from 'semantic-ui-react';
import PropTypes from 'prop-types';


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
                        <Icon name="long arrow right"/>
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
                    <Icon name="long arrow right"/>
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
);

FlightSection.propTypes = {
  firstHour: PropTypes.string.isRequired,
  secondHour: PropTypes.string.isRequired,
  firstCity: PropTypes.string.isRequired,
  secondCity: PropTypes.string.isRequired
};

export default FlightSection