import React from "react";
import TextMenu from "../Common/TextMenu";
import travelOptions from "../../data/travelOptions";
import TravelerMenu from "../Avail/TravelerMenu";
import DropCabinMenu from "../Common/DropCabinMenu";
import SearchSection from "./SearchSection";
import SearchForm from "./SearchForm";

import {Button, Dimmer, Dropdown, Grid, Icon, Input, Loader, Segment} from "semantic-ui-react";
import DatePicker from "react-datepicker";
import {configTravelString} from "../../util/travelConfig";
import "react-datepicker/dist/react-datepicker.css";

import {getIata} from "../../util/matcher";
import {handleRequestData} from "../../util/handleRequestData";

class TravelSearch extends React.Component {

  render() {


    const {config, filters,loading, actions} = this.props;


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
      cabin: config.get('cabin'),
      destinies: config.get('destinies'),
      limit: filters.getIn(['filters','limit'])
    };


    const submit = (values) => {
      actions.availableFlights(handleRequestData(values, properties.selectedType, properties.travelers, properties.cabin, properties.limit));
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


    function hideOn(types) {
      for (let i = 0; i < types.length; i++) {
        if (properties.selectedType === types[i]) {
          return "hiddenComponent"
        }
      }
      return "";
    }

    return (
      <Segment raised className='travelSearchSegment'>


        <Dimmer active={loading} inverted>
          <Loader content='Loading'/>
        </Dimmer>

        <Grid columns={3} centered verticalAlign="middle">
          <Grid.Column textAlign="center">
            <Button.Group className={hideOn(['roundTrip', 'oneWay'])}>
              <Button onClick={(e, data) => actions.removeDestiny()}> - </Button>
              <Button.Or text={properties.destinies.size}/>
              <Button onClick={(e, data) => actions.addDestiny()}> + </Button>
            </Button.Group>
          </Grid.Column>

          <Grid.Column textAlign="center" style={{display: "flex", alignItems: "center"}}>
            <TextMenu options={properties.typeOptions}
                      selected={properties.selectedType}
                      selectType={actions.typeChange}/>
          </Grid.Column>

          <Grid.Column textAlign="center">
            <Dropdown
              text={configTravelString(config, travelOptions)}
              floating
              pointing
              closeOnBlur={false}
              closeOnChange={false}>
              <Dropdown.Menu onClick={e => e.stopPropagation()}>
                <Dropdown.Header content="Cabin"/>
                <Dropdown.Item>
                  <DropCabinMenu
                    config={config}
                    options={travelOptions}
                    cabinChange={(cabin) => actions.cabinChange(cabin)}/>
                </Dropdown.Item>
                <Dropdown.Divider/>
                <Dropdown.Header content="Passengers"/>
                <Segment basic>
                  <TravelerMenu
                    config={config}
                    options={travelOptions}
                    travelerTypeCountChange={(travelerType, value) => actions.travelerTypeCountChange(travelerType, value, properties.travelers)}
                  />
                </Segment>
              </Dropdown.Menu>
            </Dropdown>
          </Grid.Column>

          <Grid.Row stretched verticalAlign="middle"
                    className={hideOn(['multiCity'])}>
            <div className="search-field">
              <SearchSection properties={properties} actions={actions}/>
            </div>

            <DatePicker className={hideOn(['oneWay'])}
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
                  cabin: properties.cabin,
                  limit: properties.limit
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

          <Grid.Row className={hideOn(['roundTrip', 'oneWay'])}>
            <Grid.Row>
              <SearchForm
                onSubmit={submit}
                properties={properties}
                actions={actions}/>
            </Grid.Row>
          </Grid.Row>
        </Grid>
      </Segment>
    );
  }
}
export default TravelSearch;
