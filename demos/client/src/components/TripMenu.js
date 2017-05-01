import React from 'react'
import { Menu } from 'semantic-ui-react'

const TripMenu = ({activeItem, handleItemClick}) => (
    <Menu text>
        <Menu.Item name='roundTrip' active={activeItem === 'roundTrip'}
                   onClick={() => handleItemClick('roundTrip')} />
        <Menu.Item name='oneWay' active={activeItem === 'oneWay'}
                   onClick={() => handleItemClick('oneWay')} />
    </Menu>
);

export default TripMenu;
