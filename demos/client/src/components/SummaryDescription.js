import React from 'react';
import {Container, Header, Divider} from 'semantic-ui-react';

const SummaryDescription = () => (
    <Container text>
        <Header as='h2'>You are flying Aeromexico</Header>
        <Divider/>
        <strong>Hermosillo, SO, Mexico (HMO) to Tokyo, Japan (NRT)</strong><br/>
        Aeromexico, round-trip, economy, 2 adults<br/>
        <br/>
        Depart: Wed May 17 2017<br/>
        Return: Fri May 26 2017
    </Container>
);

export default SummaryDescription;