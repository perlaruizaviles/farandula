import React from "react";
import TextMenu from "../Common/TextMenu";
import DateSelector from "../Common/DateSelector";
import AirportSearch from "./AirportSearch";
import ExchangeButton from "../Common/ExchangeButton";
import travelOptions from "../../data/travelOptions";
import DropTravelMenu from "../Common/DropTravelMenu";


import {Button, Dimmer, Grid, Icon, Loader, Segment} from "semantic-ui-react";
import {getIata} from "../../util/matcher";

class TravelSearch extends React.Component {

  render() {


    const {config, loading, typeChange, dateChange, travelerTypeCountChange, cabinChange, searchAirport, fromAirportChange, toAirportChange, exchangeDestinations, availableFlights, cleanField} = this.props;


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
                      selectType={typeChange}/>
          </Grid.Row>
          <Grid.Row stretched verticalAlign="middle">
              <div className="search-field">
                <AirportSearch
                  searchChange={(query, quantum = properties.airportTo) => searchAirport(query, quantum)}
                  changeSelected={value => fromAirportChange(value)}
                  airports={properties.airports}
                  value={properties.airportFrom.title}
                  cleanField={(quantum = 'from') => cleanField(quantum)}/>

                <ExchangeButton handleExchange={
                  (from = properties.airportFrom, to = properties.airportTo) => exchangeDestinations(from, to)}/>

                <AirportSearch
                  searchChange={(query, quantum = properties.airportFrom) => searchAirport(query, quantum)}
                  changeSelected={value => toAirportChange(value)}
                  airports={properties.airports}
                  value={properties.airportTo.title}
                  cleanField={(quantum = 'to') => cleanField(quantum)}/>
              </div>
              <div className="search-field">
                <DateSelector minDate={properties.minDate}
                              selectsStart
                              maxDate={properties.maxDate}
                              startDate={properties.startDate}
                              endDate={properties.endDate}
                              selected={properties.startDate}
                              changeTravelDate={date => dateChange('depart', date)}/>

                <DateSelector styles={(properties.selectedType === 'oneWay')? "hiddenComponent":""}
                              minDate={properties.minDate}
                              selectsEnd
                              maxDate={properties.maxDate}
                              startDate={properties.startDate}
                              endDate={properties.endDate}
                              selected={properties.endDate}
                              changeTravelDate={date => dateChange('return', date)}/>
              </div>
              <div className="search-field">
                <DropTravelMenu
                  config={config}
                  options={travelOptions}
                  travelerTypeCountChange={(travelerType, count) => travelerTypeCountChange(travelerType, count)}
                  cabinChange={cabinChange}/>

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
                    availableFlights(search);
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