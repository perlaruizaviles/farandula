import React from "react";
import {Dropdown, Segment} from "semantic-ui-react";
import DropCabinMenu from "./DropCabinMenu";
import TravelerMenu from "../Avail/TravelerMenu";
import {configTravelString} from "../../util/travelConfig";
import PropTypes from "prop-types";


const DropTravelMenu = ({options, config, travelerTypeCountChange, cabinChange}) => (
  <Dropdown
    text={configTravelString(config, options)}
    floating
    pointing
    closeOnBlur={false}
    closeOnChange={false}
    style={{width: 150}}>
    <Dropdown.Menu onClick={e => e.stopPropagation()}>
      <Dropdown.Header content="Cabin"/>
      <Dropdown.Item>
        <DropCabinMenu
          config={config}
          options={options}
          cabinChange={(cabin) => cabinChange(cabin)}/>
      </Dropdown.Item>
      <Dropdown.Divider/>
      <Dropdown.Header content="Passengers"/>
      <Segment basic>
        <TravelerMenu
          config={config}
          options={options}
          travelerTypeCountChange={travelerTypeCountChange}/>
      </Segment>
    </Dropdown.Menu>
  </Dropdown>
);

DropTravelMenu.propTypes = {
  options: PropTypes.object.isRequired,
  config: PropTypes.object.isRequired,
  travelerTypeCountChange: PropTypes.func.isRequired,
  cabinChange: PropTypes.func.isRequired
};

export default DropTravelMenu;