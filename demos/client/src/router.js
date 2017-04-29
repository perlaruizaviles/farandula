import React from 'react';
import {syncHistoryWithStore} from 'react-router-redux';
import {Router, Route, browserHistory, IndexRedirect} from 'react-router';

import Main from './components/Main';
import Welcome from './components/Welcome';
import ComponentShowcase from './components/ComponentShowcase';
import AirportFieldDemo from './components/AirportField.demo';
import FlightOptionsMenuDemo from './containers/FlightOptionsMenu';
import TripMenuDemo from './components/TripMenu.demo';
import DateFlightPickerDemo from './components/DateFlightPicker.demo';

const makeRouter = store => {
  let history = syncHistoryWithStore(browserHistory, store);
  return (
    <Router history={history}>
      <Route path="/" component={Main}>
        <IndexRedirect to="/home"/>
        <Route path="/home" component={Welcome}/>
        <Route path="/components" component={ComponentShowcase}>
          <IndexRedirect to="/components/airport-field"/>
          <Route path="/components/airport-field" component={AirportFieldDemo}/>
          <Route path="/components/flight-options" component={FlightOptionsMenuDemo}/>
          <Route path="/components/trip-menu" component={TripMenuDemo}/>
          <Route path="/components/date-flight-picker" component={DateFlightPickerDemo}/>
        </Route>
      </Route>
    </Router>
  )
};

export default makeRouter;
