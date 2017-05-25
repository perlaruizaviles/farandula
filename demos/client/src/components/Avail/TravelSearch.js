import React from "react";
import TextMenu from "../Common/TextMenu";
import travelOptions from "../../data/travelOptions";
import DropTravelMenu from "../Common/DropTravelMenu";

import {Button, Dimmer, Grid, Icon, Input, Loader, Search, Segment} from "semantic-ui-react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

import {getIata} from "../../util/matcher";

class TravelSearch extends React.Component {

  render() {


    const {config, loading, actions} = this.props;


    const properties = {
      selectedType: config.get('type'),
      typeOptions: travelOptions.get('type'),
      airports: config.get('airports'),
      airportFrom: config.getIn(['locations', 'from']),
      airportTo: config.getIn(['locations', 'to']),
      minDate: travelOptions.get('minDate'),
      maxDate: travelOptions.get('maxDate'),
      startDate: config.getIn(['dates', 'depart']),
      endDate: config.getIn(['dates', 'return']),
      travelers: config.get('travelers'),
      cabin: config.get('cabin')
    };

    const isInvalidForm = (properties) => {
      if (properties.airportFrom.title === undefined) {
        return true;
      }
      if (properties.airportTo.title === undefined) {
        return true;
      }
      return properties.startDate === undefined;

    };


    return (
      <Segment raised className='travelSearchSegment'>
        <Dimmer active={loading} inverted>
          <Loader content='Loading'/>
        </Dimmer>

        <Grid columns={1} centered>
          <Grid.Row>
            <TextMenu options={properties.typeOptions}
                      selected={properties.selectedType}
                      selectType={actions.typeChange}/>
          </Grid.Row>
          <Grid.Row stretched verticalAlign="middle">
            <div className="search-field">

              <Search style={{display: 'inline'}}
                      onSearchChange={(e, query, quantum = properties.airportTo) => actions.searchAirport(query, quantum)}
                      onResultSelect={(e, value) => actions.fromAirportChange(value)}
                      results={properties.airports}
                      value={properties.airportFrom.title}
                      onMouseDown={(e, quantum = 'from') => actions.cleanField(quantum)}
              />

              <Button icon
                      onClick={(event, data, from = properties.airportFrom, to = properties.airportTo) => actions.exchangeDestinations(from, to)}>
                <Icon name='exchange'/>
              </Button>

              <Search style={{display: 'inline'}}
                      onSearchChange={(e, query, quantum = properties.airportFrom) => actions.searchAirport(query, quantum)}
                      onResultSelect={(e, value) => actions.toAirportChange(value)}
                      results={properties.airports}
                      value={properties.airportTo.title}
                      onMouseDown={(e, quantum = 'to') => actions.cleanField(quantum)}
              />
            </div>
            <div className="search-field">

              <DatePicker customInput={<Input icon="calendar outline" style={{width: '150px', color: '#216ba5'}}/>}
                          selectsStart
                          minDate={properties.minDate}
                          maxDate={properties.maxDate}
                          selected={properties.startDate}
                          startDate={properties.startDate}
                          endDate={properties.endDate}
                          placeholderText="Select date..."
                          onChange={date => actions.dateChange('depart', date)}/>

              <DatePicker className={(properties.selectedType === 'oneWay') ? "hiddenComponent" : ""}
                          customInput={<Input icon="calendar outline" style={{width: '150px', color: '#216ba5'}}/>}
                          selectsEnd
                          minDate={properties.minDate}
                          maxDate={properties.maxDate}
                          selected={properties.endDate}
                          startDate={properties.startDate}
                          endDate={properties.endDate}
                          placeholderText="Select date..."
                          onChange={date => actions.dateChange('return', date)}/>
            </div>
            <div className="search-field">
              <DropTravelMenu
                config={config}
                options={travelOptions}
                travelerTypeCountChange={(travelerType, count) => actions.travelerTypeCountChange(travelerType, count)}
                cabinChange={actions.cabinChange}/>

              <Button animated disabled={isInvalidForm(properties)} className='orange' onClick={
                (event, button, search = {
                  departureAirport: getIata(properties.airportFrom.title),
                  departingDate: properties.startDate.format('YYYY-MM-DD'),
                  departingTime: "10:15:30",
                  arrivalAirport: getIata(properties.airportTo.title),
                  arrivalDate: properties.endDate.format('YYYY-MM-DD'),
                  arrivalTime: "00:00:00",
                  type: properties.selectedType,
                  passenger: properties.travelers,
                  cabin: properties.cabin
                }) => {
                  actions.availableFlights(search);
                }
              }>
                <Button.Content visible>Search</Button.Content>
                <Button.Content hidden>
                  <Icon name='plane' className='large'/>
                </Button.Content>
              </Button>
            </div>
          </Grid.Row>
        </Grid>
      </Segment>
    );
  }
}
export default TravelSearch;
