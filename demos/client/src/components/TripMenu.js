import React from 'react'
import { Menu } from 'semantic-ui-react'

const TripMenu = ({activeItem, handleItemClick}) => (
    <Menu text>
        <Menu.Item name='roundTrip' active={activeItem === 'roundTrip'}
                   onClick={(e, {name}) => handleItemClick(name)} />
        <Menu.Item name='oneWay' active={activeItem === 'oneWay'}
                   onClick={(e, {name}) => handleItemClick(name)} />
    </Menu>
);

export default TripMenu;
