import React from 'react';
import {Divider} from 'semantic-ui-react';

const FlightDetail = () => (
    <div>
        <strong>Depart &mdash; Sat, Jun 16</strong><span style={{float:'right'}}>46h 05m</span>
        <Divider/>
        <p>
            <strong>4:15p &mdash; 8:10p</strong><br/><span style={{float:'right'}}>Economy 1h 55h</span>
            Hermosillo (HMO) &mdash; Moterrey (GDL)<br/>
            Aeromexico 2817 - Narrow-body jet<br/>
            Operated by Aerolitoral DBA Aeromexico Connect
        </p>
        <Divider/>
    </div>
);

export default FlightDetail;