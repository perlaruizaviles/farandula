/**
 * Created by antoniohernandez on 5/1/17.
 */

import React from 'react'
import { Label, Button, Divider } from 'semantic-ui-react'

const FlightInfo = () => (
    <div>
        <Label pointing='right'>7:46 CUU to MTY 9:55</Label>
        <Divider horizontal>Return</Divider>
        <Label pointing='right'>7:46 MTY to CUU 9:55</Label>
    </div>
)

export default FlightInfo