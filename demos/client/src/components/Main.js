import React from 'react';
import {Menu, Segment} from 'semantic-ui-react';

const Main = ({router: {push}, location: {pathname},
                ...props}) => (
  <div>
    <Menu attached="top" size="large" inverted>
      <Menu.Item header onClick={() => push('/')}>
        Quantum Farandula
      </Menu.Item>
      <Menu.Item active={pathname==='/summary'}
                 onClick={() => push('/summary')}>
        Summary
      </Menu.Item>

    </Menu>
    <Segment attached>
      {props.children}
    </Segment>
  </div>
);

export default Main;