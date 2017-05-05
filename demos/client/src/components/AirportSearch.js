import React from 'react';
import {Search} from 'semantic-ui-react';

const AirportSearch = ({changeSelected, searchChange, airports}) => {
    return(
       <Search
        onSearchChange={(e, query) => searchChange(query)}
        onResultSelect={(e, value) => changeSelected(value)}
        results={airports}
       />
    );
};

export default AirportSearch;