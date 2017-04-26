import React from 'react';
import {syncHistoryWithStore} from 'react-router-redux';
import {Router, Route, browserHistory, IndexRedirect} from 'react-router';

import Main from './components/Main';
import Welcome from './components/Welcome';
import ComponentShowcase from './components/ComponentShowcase';
import AirportFieldDemo from './components/AirportField.demo';

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
        </Route>
      </Route>
    </Router>
  )
};

export default makeRouter;