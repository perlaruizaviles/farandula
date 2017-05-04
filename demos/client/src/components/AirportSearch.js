import React from 'react';
import {Search} from 'semantic-ui-react';

const AirportSearch = ({changeSelected, searchChange, results, airport}) => {
    return(
       <Search
        onSearchChange={searchChange}
        onResultSelect={changeSelected}
        results={results}
        value={airport}
       />
    );
};

export default AirportSearch;