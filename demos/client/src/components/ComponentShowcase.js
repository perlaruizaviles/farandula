import React from 'react';
import {Menu, Segment} from 'semantic-ui-react';

const ComponentShowcase = ({router: {push}, location: {pathname},
                             ...props}) => (
  <div>
    <Menu>
    </Menu>
    <Segment>
      {props.children}
    </Segment>
  </div>
);

export default ComponentShowcase;
