import React from "react";
import TextMenu from "./TextMenu";
import DateSelector from "./DateSelector";
import AirportSearch from "./AirportSearch";
import ExchangeButton from "./ExchangeButton";
import travelOptions from "../data/travelOptions";
import DropTravelMenu from "./DropTravelMenu";


import {Button, Dimmer, Icon, Loader} from "semantic-ui-react";
import {getIata} from "../util/matcher";

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
      travelers: config.get('travelers')
    };

    const isInvalidForm = (properties) => {
      if (properties.airportFrom.title === undefined) {
        return true
      }
      if (properties.airportTo.title === undefined) {
        return true
      }
      if (properties.startDate === undefined) {
        return true
      }
      return properties.endDate === undefined;

    };


    return (
      <div>
        <Dimmer active={loading} inverted>
          <Loader content='Loading'/>
        </Dimmer>

        <TextMenu options={properties.typeOptions}
                  selected={properties.selectedType}
                  selectType={typeChange}/>
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

        <DateSelector minDate={properties.minDate}
                      selectsStart
                      maxDate={properties.maxDate}
                      startDate={properties.startDate}
                      endDate={properties.endDate}
                      selected={properties.startDate}
                      changeTravelDate={date => dateChange('depart', date)}/>

        <DateSelector minDate={properties.minDate}
                      selectsEnd
                      maxDate={properties.maxDate}
                      startDate={properties.startDate}
                      endDate={properties.endDate}
                      selected={properties.endDate}
                      changeTravelDate={date => dateChange('return', date)}/>

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
            passenger: properties.travelers
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
    );
  }
}
export default TravelSearch;
