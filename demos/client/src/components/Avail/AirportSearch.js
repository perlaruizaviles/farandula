import React from "react";
import {Search} from "semantic-ui-react";
import PropTypes from "prop-types";

const AirportSearch = ({changeSelected, searchChange, airports, value, cleanField}) => {
    return(
       <Search style={{display:'inline'}}
        onSearchChange={(e, query) => searchChange(query)}
        onResultSelect={(e, value) => changeSelected(value)}
        results={airports}
        value={value}
        onMouseDown={(e) => cleanField()}
       />
    );
};

AirportSearch.propTypes = {
  changeSelected: PropTypes.func.isRequired,
  searchChange: PropTypes.func.isRequired,
  airports: PropTypes.array,
  value: PropTypes.string,
  cleanField: PropTypes.func.isRequired
};

export default AirportSearch;