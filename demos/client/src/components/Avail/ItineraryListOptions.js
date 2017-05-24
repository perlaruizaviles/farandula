import React from "react";
import {Dropdown} from "semantic-ui-react";
import resultsOptions from "../../data/resultsOptions";

const ItineraryListOptions = ({ order, changeOrder }) => {
    return (
    <Dropdown text={ order } icon='sort' floating labeled button className='icon'>
        <Dropdown.Menu>
            {resultsOptions.get('order').map((option) => <Dropdown.Item key={ option } onClick={() => changeOrder(option)}> { option } </Dropdown.Item>)}
        </Dropdown.Menu>
    </Dropdown>

)};

export default ItineraryListOptions;