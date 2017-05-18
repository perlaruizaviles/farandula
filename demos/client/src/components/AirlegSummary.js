import React from 'react';
import {Icon} from 'semantic-ui-react';

const AirlegSummary = () => (
    <p>
        7:46a &emsp; <strong>MEX</strong> 
        &emsp;<Icon name="long arrow right"/>&emsp; 
        9:55p &emsp; <strong>NRT</strong> &emsp;
        <span style={{color:'gray'}}>16h 00m</span>&emsp;
        <span style={{color:'gray'}}>1 stop</span>&emsp;
        <span style={{color:'gray'}}>(MEX, NRT)</span>
    </p>
)

export default AirlegSummary;