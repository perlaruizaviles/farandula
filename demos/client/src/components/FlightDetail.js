import React from 'react';
import {Grid, Button, Divider} from 'semantic-ui-react';

const FlightDetail = () => (
    <div>
        <Divider/>
        <Grid columns={3}>
            <Grid.Column textAlign='center'>
                <Button>Select</Button>
            </Grid.Column>
            <Grid.Column style={{width:'66%'}}>
                <strong>Depart &mdash; Sat, Jun 16</strong><span style={{float:'right'}}>46h 05m</span>
                <Divider/>
                <p>
                    <strong>4:15p &mdash; 8:10p</strong><br/><span style={{float:'right'}}>Economy 1h 55h</span>
                    Hermosillo (HMO) &mdash; Moterrey (GDL)<br/>
                    Aeromexico 2817 - Narrow-body jet<br/>
                    Operated by Aerolitoral DBA Aeromexico Connect
                </p>
                <Divider/>
                Change planes in Monterrey (MTY) <span style={{color:'orange'}}>Long layover</span><span style={{float:'right'}}>14h 03m</span>
                <Divider/>
                <p>
                    <strong>4:15p &mdash; 8:10p</strong><br/><span style={{float:'right'}}>Economy 1h 55h</span>
                    Hermosillo (HMO) &mdash; Moterrey (GDL)<br/>
                    Aeromexico 2817 - Narrow-body jet<br/>
                    Operated by Aerolitoral DBA Aeromexico Connect
                </p>
            </Grid.Column>
        </Grid>
    </div>
);

export default FlightDetail;