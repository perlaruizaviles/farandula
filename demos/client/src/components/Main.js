import React from "react";
import {Menu, Segment} from "semantic-ui-react";
import * as routes from "../routes";

const Main = ({router: {push}, location: {pathname},
                ...props}) => (
  <div>
    <Menu attached="top" size="large" inverted>
      <Menu.Item header onClick={() => push(routes.HOME)}>
        Quantum Farandula
      </Menu.Item>
      <Menu.Item active={pathname === routes.SUMMARY}
                 onClick={() => push(routes.SUMMARY)}>
        Summary
      </Menu.Item>
    </Menu>
    <Segment attached>
      {props.children}
    </Segment>
  </div>
);

export default Main;