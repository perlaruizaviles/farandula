import React from 'react';
import {Dropdown} from 'semantic-ui-react';
import {titleize} from 'inflection';

const DropCabinMenu = ({options, config}) => (
    <Dropdown text={config.get('cabin')} fluid>
        <Dropdown.Menu>
            {
                options.get('cabin')
                .map(cabin => {
                    console.log(cabin);
                    return (
                        <Dropdown.Item key={cabin} onClick={()=>console.log("yep")}>{cabin}</Dropdown.Item>
                    )
                })
            }
        </Dropdown.Menu>
    </Dropdown>
);

export default DropCabinMenu;