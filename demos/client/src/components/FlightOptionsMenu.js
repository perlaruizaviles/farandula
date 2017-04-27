import React from 'react';
import {findOptionById} from '../util/flightOptions';
import * as inf from 'inflection';
import {Dropdown, Grid} from 'semantic-ui-react';
import {countPassengers} from '../util/flightSettings';

const settingsString = (settings, options) => {
  let count = countPassengers(settings);
  let cabinId = settings.get('cabin');
  let cabinName = inf.titleize(findOptionById(options, 'cabin', cabinId)
    .get('name'));
  if (count < 1)
    throw new Error('malformed flightSettings: passenger count < 1');
  else if (count === 1) {
    let passengerId = settings.get('passengers')
      .filter(count => count !== 0)
      .keySeq().get(0);
    let passengerName = findOptionById(options, 'passengers', passengerId)
      .get('name');
    return '1 ' + passengerName + ', ' + cabinName;
  } else {
    let passengerIds = settings.get('passengers')
      .filter(count => count !== 0)
      .keySeq();
    if (passengerIds.count() === 1) {
      let passengerName = inf.pluralize(findOptionById(options, 'passengers', passengerIds.get(0)).get('name'));
      return count + ' ' + passengerName + ', ' + cabinName;
    } else {
      return count + ' travelers, ' + cabinName;
    }
  }
};

const FlightOptionsMenu = ({settings, options, ...props}) => (
  <Dropdown text={settingsString(settings, options)}
            pointing floating button closeOnBlur={false}>
    <Dropdown.Menu>
      <Dropdown.Header content="cabin"/>
      <Dropdown.Item>
        <Dropdown text={inf.titleize(findOptionById(options, 'cabin', settings.get('cabin')).get('name'))}
                  fluid closeOnBlur={false}>
          <Dropdown.Menu>
            {options.get('cabin')
              .map(cabin =>
                (<Dropdown.Item key={cabin.get('id')}>
                  {inf.titleize(cabin.get('name'))}
                  </Dropdown.Item>))}
          </Dropdown.Menu>
        </Dropdown>
      </Dropdown.Item>
      <Dropdown.Divider/>
      <Dropdown.Header content="passengers"/>
      <Dropdown.Item>
        <Grid columns='equal'>
          {settings.get('passengers')
            .map((count,id) =>
              (<Grid.Row key={id}>
                  <Grid.Column width={7}>
                    {inf.titleize(inf.pluralize(findOptionById(options,'passengers',id).get('name')))}
                  </Grid.Column>
                  <Grid.Column><button>-</button></Grid.Column>
                  <Grid.Column>{count}</Grid.Column>
                  <Grid.Column><button>+</button></Grid.Column>
                  <Grid.Column>
                    {findOptionById(options, 'passengers', id).get('ageRange').get(0)} -
                    {findOptionById(options, 'passengers', id).get('ageRange').get(1)}
                  </Grid.Column>
                </Grid.Row>
              )).valueSeq()}
        </Grid>
      </Dropdown.Item>
    </Dropdown.Menu>
  </Dropdown>
);

export {FlightOptionsMenu as default, settingsString};