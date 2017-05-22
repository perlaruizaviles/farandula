import React from 'react';
import {Card, Dropdown} from 'semantic-ui-react';

const OptionsTravel = ({options, config}) => {
    return(
    <Card className='orange'  style={{display:'none'}}>
        <Card.Content>
            <Card.Header>
                Options
            </Card.Header>
        </Card.Content>
        <Card.Content>
            <Dropdown>
                <Dropdown text={config}/>
            </Dropdown>
        </Card.Content>
    </Card>
)};

export default OptionsTravel;