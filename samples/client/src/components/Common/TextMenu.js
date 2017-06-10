import React from "react";
import {Menu} from "semantic-ui-react";
import PropTypes from "prop-types";

const TextMenu = ({options, selected, selectType}) => (
  <Menu text>
    {
      options.map(type => (
        <Menu.Item key={type} name={type} active={selected === type} onClick={() => selectType(type)}/>
      ))
    }
  </Menu>
);

TextMenu.propTypes = {
  options: PropTypes.object.isRequired,
  selected: PropTypes.string,
  selectType: PropTypes.func.isRequired
};

export default TextMenu;
