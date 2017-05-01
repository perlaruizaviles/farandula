/**
 * Created by antoniohernandez on 5/1/17.
 */
import React, { Component } from 'react'
import { Segment, Button } from 'semantic-ui-react'

const Cell = () => {
    return(
        <Segment className="raised">
            <Button basic color='olive'>Ver Vuelo</Button>Flight cell
        </Segment>
    )
}

export default Cell;