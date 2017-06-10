import React from "react";
import {Menu} from "semantic-ui-react";
import * as routes from "../routes";

const Main = ({
                router: {push}, location: {pathname},
                ...props
              }) => (
  <div>
    <Menu attached="top" size="large" inverted>
      <Menu.Item header onClick={() => push(routes.HOME)}>
        Quantum Farandula
      </Menu.Item>
      <Menu.Item active={pathname === routes.BOOK}
                 onClick={() => push(routes.BOOK)}>
        Book
      </Menu.Item>
    </Menu>
    <div>
      {props.children}
    </div>
  </div>
);

export default Main;