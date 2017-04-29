import React from 'react';
import {findOptionById} from '../util/flightOptions';
import {titleize, pluralize} from 'inflection';
import {Dropdown, Grid, Segment, Button} from 'semantic-ui-react';
import {countPassengers} from '../util/flightSettings';

const settingsString = (settings, options) => {
  let count = countPassengers(settings);
  let cabinId = settings.get('cabin');
  let cabinName = titleize(findOptionById(options, 'cabin', cabinId)
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
      let passengerName = pluralize(findOptionById(options, 'passengers', passengerIds.get(0)).get('name'));
      return count + ' ' + passengerName + ', ' + cabinName;
    } else {
      return count + ' travelers, ' + cabinName;
    }
  }
};

const ageRangeString = (ageRange) => {
  const low = ageRange.get(0);
  const high = ageRange.get(1);
  return (low <= 0 ? 'under' : low + ' -') + (isFinite(high) ? ' ' + high : '');
};

const FlightOptionsMenu = ({settings, options, onCabinClick, onMorePassengerClick, onLessPassengerClick}) => {
  const settingsDescription = settingsString(settings, options);
  const cabinOption = findOptionById(options, 'cabin', settings.get('cabin'));
  return (
    <Dropdown text={settingsDescription}
              floating pointing
              closeOnBlur={false}
              closeOnChange={false}>
      <Dropdown.Menu onClick={e => e.stopPropagation()}>
        <Dropdown.Header content="cabin"/>
        <Dropdown.Item>
          <Dropdown text={titleize(cabinOption.get('name'))} fluid>
            <Dropdown.Menu>
              {
                options.get('cabin')
                  .map(cabin => {
                    const id = cabin.get('id');
                    return (
                      <Dropdown.Item key={id} onClick={e => onCabinClick(id)}>
                        {titleize(cabin.get('name'))}
                      </Dropdown.Item>
                    )
                  }
                )
              }
            </Dropdown.Menu>
          </Dropdown>
        </Dropdown.Item>
        <Dropdown.Divider/>
        <Dropdown.Header content="passengers"/>
        <Segment basic>
          <Grid style={{width: "20em"}}>
            {
              settings.get('passengers')
                .map((count, id) => {
                  const passengerOption = findOptionById(options, 'passengers', id);
                  const ageRange = passengerOption.get('ageRange');
                  return (
                    <Grid.Row key={id} columns={3} verticalAlign="middle">
                      <Grid.Column width={6} textAlign="left">
                        {titleize(pluralize(passengerOption.get('name')))}
                      </Grid.Column>
                      <Grid.Column width={6} textAlign="center">
                        <Button.Group size="mini">
                          <Button compact onClick={() => onLessPassengerClick(id)}>-</Button>
                          <Button.Or text={count}/>
                          <Button compact onClick={() => onMorePassengerClick(id)}>+</Button>
                        </Button.Group>
                      </Grid.Column>
                      <Grid.Column width={4} textAlign="right">
                        {ageRangeString(ageRange)}
                      </Grid.Column>
                    </Grid.Row>
                  )
                }).valueSeq()
            }
          </Grid>
        </Segment>
      </Dropdown.Menu>
    </Dropdown>
  );
};

export {FlightOptionsMenu as default, settingsString, ageRangeString};