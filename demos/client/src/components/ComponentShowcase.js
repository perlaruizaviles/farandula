import React from 'react';
import {Menu, Segment} from 'semantic-ui-react';
import Component from './TravelTypeMenu.demo';

const ComponentShowcase = ({router: {push}, location: {pathname},
                             ...props}) => (
  <div>
    <Menu>
    </Menu>
    <Segment>
      <Component />
      {props.children}
    </Segment>
  </div>
);

export default ComponentShowcase;
