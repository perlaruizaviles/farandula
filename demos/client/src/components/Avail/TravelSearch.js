import React from "react";
import TextMenu from "../Common/TextMenu";
import travelOptions from "../../data/travelOptions";
import DropTravelMenu from "../Common/DropTravelMenu";
import SearchSection from "./SearchSection";

import {Button, Dimmer, Grid, Icon, Input, Loader, Segment} from "semantic-ui-react";
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
              <SearchSection properties={properties} actions={actions} />
            </div>
            <div className="search-field">
              <SearchSection properties={properties} actions={actions} />
            </div>
            <div className="search-field">
              <SearchSection properties={properties} actions={actions} />
            </div>
            <div className="search-field">
              <SearchSection properties={properties} actions={actions} />
            </div>
            <div className="search-field">
              <SearchSection properties={properties} actions={actions} />
            </div>
            <div className="search-field">
              <SearchSection properties={properties} actions={actions} />
            </div>

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
