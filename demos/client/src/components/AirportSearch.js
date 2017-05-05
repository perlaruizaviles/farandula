import React from 'react';
import {Search} from 'semantic-ui-react';

const AirportSearch = ({changeSelected, searchChange, airports, airport}) => {
    return(
       <Search
        onSearchChange={searchChange}
        onResultSelect={changeSelected}
        results={airports}
        value={airport}
       />
    );
};

export default AirportSearch;