import React from 'react';
import {Search} from 'semantic-ui-react';

const AirportSearch = ({changeSelected, searchChange, airports, value, cleanField}) => {
    return(
       <Search style={{display:'inline'}}
        onSearchChange={(e, query) => searchChange(query)}
        onResultSelect={(e, value) => changeSelected(value)}
        results={airports}
        value={value}
        minCharacters={3}
        onMouseDown={(e) => cleanField()}
       />
    );
};

export default AirportSearch;