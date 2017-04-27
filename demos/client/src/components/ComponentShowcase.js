import React from 'react';
import {Menu, Segment} from 'semantic-ui-react';

const ComponentShowcase = ({router: {push}, location: {pathname},
                             ...props}) => (
  <div>
    <Menu>
      <Menu.Item active={pathname === '/components/airport-field'}
                 onClick={() => push('/components/airport-field')}>
        Airport Field
      </Menu.Item>
      <Menu.Item active={pathname === '/components/flight-options'}
                 onClick={() => push('/components/flight-options')}>
        Flight Options Menu
      </Menu.Item>
    </Menu>
    <Segment>
      {props.children}
    </Segment>
  </div>
);

export default ComponentShowcase;