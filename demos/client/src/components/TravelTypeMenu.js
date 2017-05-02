import React from 'react';
import {Menu} from 'semantic-ui-react';

const TravelTypeMenu = ({options, selected, selectType}) => (
  <Menu text>
    {
      options.map(type => (
        <Menu.Item key={type} name={type} active={selected === type} onClick={() => selectType(type)} />
      ))
    }
  </Menu>
);

export default TravelTypeMenu;