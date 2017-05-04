import React from 'react';
import {Menu} from 'semantic-ui-react';

const TextMenu = ({options, selected, selectType}) => (
  <Menu>
    {
      options.map(type => (
        <Menu.Item key={type} name={type} active={selected === type} onClick={() => selectType(type)} />
      ))
    }
  </Menu>
);

export default TextMenu;
