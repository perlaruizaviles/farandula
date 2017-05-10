import React from 'react';
import TravelerForm from './TravelerForm';
import BillingForm from './BillingForm';
import {Grid, Divider, Button, Container} from 'semantic-ui-react'; 
import SummaryCard from './SummaryCard';
import SummaryDescription from './SummaryDescription';

export default () => (
    <Grid>
        <Grid.Row>
            <Grid.Column width={11}>
                <Container text>
                    <Grid.Row>
                        <SummaryDescription/>
                    </Grid.Row>
                    <Divider />
                    <Grid.Row>
                        <TravelerForm travelerNum={1} />
                    </Grid.Row>
                    <Grid.Row>
                        <TravelerForm travelerNum={2} />
                    </Grid.Row>
                    <Divider />
                    <Grid.Row>
                        <BillingForm />    
                    </Grid.Row> 
                    <Grid.Row>
                        <Button color='orange'>Book for USD 1847.98</Button>
                    </Grid.Row>    
                </Container>
                   
            </Grid.Column>
            <Grid.Column width={5}>
                <SummaryCard />
            </Grid.Column>
        </Grid.Row>
    </Grid>
)