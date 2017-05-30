import React from 'react';
import {Search, Input, Button, Icon} from 'semantic-ui-react';
import DatePicker from "react-datepicker";


class SearchSection extends React.Component {
  render(){

    const {properties, actions} = this.props;

    return(
      <div className="searchSection">
        <Search style={{display: 'inline'}}
                onSearchChange={(e, query, quantum = properties.airportTo) => actions.searchAirport(query, quantum)}
                onResultSelect={(e, value) => actions.fromAirportChange(value)}
                results={properties.airports}
                value={properties.airportFrom.title}
                onMouseDown={(e,b, quantum = 'from') => actions.cleanField(quantum)}/>

        <Button icon
                onClick={(event, data, from = properties.airportFrom, to = properties.airportTo) => actions.exchangeDestinations(from, to)}>
          <Icon name='exchange'/>
        </Button>

        <Search style={{display: 'inline'}}
                onSearchChange={(e, query, quantum = properties.airportFrom) => actions.searchAirport(query, quantum)}
                onResultSelect={(e, value) => actions.toAirportChange(value)}
                results={properties.airports}
                value={properties.airportTo.title}
                onMouseDown={(e,b, quantum = 'to') => actions.cleanField(quantum)}/>

        <DatePicker customInput={<Input icon="calendar outline" style={{width: '150px', color: '#216ba5'}}/>}
                    selectsStart
                    minDate={properties.minDate}
                    maxDate={properties.maxDate}
                    selected={properties.startDate}
                    startDate={properties.startDate}
                    endDate={properties.endDate}
                    placeholderText="Select date..."
                    onChange={date => actions.dateChange('depart', date)}/>
      </div>
    );
  }
}

export default SearchSection;