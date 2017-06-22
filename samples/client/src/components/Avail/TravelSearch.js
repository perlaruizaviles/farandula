import React from "react";
import TextMenu from "../Common/TextMenu";
import travelOptions from "../../data/travelOptions";
import TravelerMenu from "../Common/TravelerMenu";
import DropCabinMenu from "../Common/DropCabinMenu";
import SearchForm from "./SearchForm";

import {Button, Dropdown, Grid, Segment} from "semantic-ui-react";
import {configTravelString} from "../../util/travelConfig";
import "react-datepicker/dist/react-datepicker.css";

import {handleRequestData} from "../../util/handleRequestData";

class TravelSearch extends React.Component {

  render() {


    const {config, filters, actions} = this.props;


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


    function hideOn(types) {
      for (let i = 0; i < types.length; i++) {
        if (properties.selectedType === types[i]) {
          return "hiddenComponent"
        }
      }
      return "";
    }

    function renderTypeOfTrip(type) {

      switch (type){
        case "multiCity":
          return <Grid.Row>
            <SearchForm
              onSubmit={submit}
              properties={properties}
              destinies={properties.destinies}
              actions={actions}/>
          </Grid.Row>;

        default:
          return <Grid.Row>
            <SearchForm
              onSubmit={submit}
              properties={properties}
              destinies={[0]}
              actions={actions}/>
          </Grid.Row>;
      }

    }


    return (
      <Segment raised className='travelSearchSegment'>

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

          {renderTypeOfTrip(properties.selectedType)}

        </Grid>
      </Segment>
    );
  }
}
export default TravelSearch;
