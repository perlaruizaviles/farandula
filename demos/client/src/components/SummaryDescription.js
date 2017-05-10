import React from 'react';
import {Container, Header, Divider} from 'semantic-ui-react';

const SummaryDescription = () => (
    <Container text>
        <Header as='h2'>You are flying Aeromexico</Header>
        <Divider/>
        <p>
            <strong>Hermosillo, SO, Mexico (HMO) to Tokyo, Japan (NRT)</strong><br/>
            Aeromexico, round-trip, economy, 2 adults
        </p>
        <br/>
        <p>
            Depart: Wed May 17 2017<br/>
            Return: Fri May 26 2017
        </p>
    </Container>
);

export default SummaryDescription;