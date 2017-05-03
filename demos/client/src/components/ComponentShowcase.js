import React from 'react';
import {Menu, Segment} from 'semantic-ui-react';
import Component from '../containers/TravelSearch';

const ComponentShowcase = ({router: {push}, location: {pathname},
                             ...props}) => (
  <div>
    <Segment>
      <Component />
      {props.children}
    </Segment>
  </div>
);

export default ComponentShowcase;
