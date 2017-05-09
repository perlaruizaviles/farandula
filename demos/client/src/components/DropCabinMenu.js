import React from 'react';
import {Dropdown} from 'semantic-ui-react';
import {titleize} from 'inflection';

const DropCabinMenu = ({options, config, cabinChange}) => (
    <Dropdown text={titleize(config.get('cabin'))} fluid>
        <Dropdown.Menu>
            {options
                .get('cabin')
                .map(cabin => <Dropdown.Item key={cabin} onClick={() => cabinChange(cabin)}>{titleize(cabin)}</Dropdown.Item>)}
        </Dropdown.Menu>
    </Dropdown>
);

export default DropCabinMenu;