import React from 'react';
import { Grid } from 'semantic-ui-react';
import {titleize, pluralize} from 'inflection';
import CounterButtons from './CounterButtons';

const TravelerMenu = ({config, options}) => (
    <Grid style={{width:"20em"}}>
        {
            config.get('travelers')
            .map((count, id) => {
                const rangeAge = options.get('travelers').get(id).get(0)
                                .toString()
                                .concat(" - ")
                                .concat(options.get('travelers').get(id).get(1));
                
                return (
                    <Grid.Row columns={3} verticalAlign="middle">
                        <Grid.Column width={6} textAlign="left">
                            {titleize(pluralize(id))}
                        </Grid.Column>
                        <Grid.Column width={6} textAlign="center">
                            <CounterButtons count={count}
                            typePassanger={id}
                            decreasePassanger={(x,y) => console.log(x,y)}
                            increasePassanger={(x,y) => console.log(x,y)}/>
                        </Grid.Column>
                        <Grid.Column width={4} textAlign="right">
                            {rangeAge}
                        </Grid.Column>
                    </Grid.Row>
                );
            })
        }
    </Grid>
);

export default TravelerMenu;