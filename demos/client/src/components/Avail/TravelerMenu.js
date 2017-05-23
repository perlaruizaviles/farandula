import React from 'react';
import {Grid} from 'semantic-ui-react';
import {titleize, pluralize} from 'inflection';
import CounterButtons from '../Common/CounterButtons';
import PropTypes from 'prop-types';

const TravelerMenu = ({config, options, travelerTypeCountChange}) => (
    <Grid style={{
        width: "20em"
    }}>
        {config
            .get('travelers')
            .map((count, id) => {
                const ages = options
                    .get('travelers')
                    .get(id);
                const ageRange = ages
                    .get(0)
                    .toString()
                    .concat(!isFinite(ages.get(1))? "+" : "-" + ages.get(1));
                return (
                    <Grid.Row key={id} columns={3} verticalAlign="middle">
                        <Grid.Column width={6} textAlign="left">
                            {titleize(pluralize(id))}
                        </Grid.Column>
                        <Grid.Column width={6} textAlign="center">
                            <CounterButtons
                                count={count}
                                typeTraveler={id}
                                travelerTypeCountChange={(travelerType, count) => travelerTypeCountChange(travelerType, count)}/>
                        </Grid.Column>
                        <Grid.Column width={4} textAlign="right">
                            {ageRange}
                        </Grid.Column>
                    </Grid.Row>
                )
            })
            .valueSeq()}
    </Grid>
);

TravelerMenu.propTypes = {
  config: PropTypes.object.isRequired,
  options: PropTypes.object.isRequired,
  travelerTypeCountChange: PropTypes.func.isRequired
};

export default TravelerMenu;