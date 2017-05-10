import React from 'react';
import {syncHistoryWithStore} from 'react-router-redux';
import {Router, Route, browserHistory, IndexRedirect} from 'react-router';

import Main from './components/Main';
import Welcome from './components/Welcome';
import ComponentShowcase from './components/ComponentShowcase';
import Summary from './components/Summary';
import Results from './components/ResultsPage';
import FlightResults from './components/FlightResults';


const makeRouter = store => {
  let history = syncHistoryWithStore(browserHistory, store);
  return (
    <Router history={history}>
      <Route path="/" component={Main}>
        <IndexRedirect to="/home"/>
        <Route path="/home" component={Welcome}/>
        <Route path="/components" component={ComponentShowcase}>
        </Route>
        <IndexRedirect to="/results"/>
        <Route path="/results" component={Results}/>
        <Route path="/flightresults" component={FlightResults}/>
        <Route path="/summary" component={Summary}/>
      </Route>
    </Router>
  )
};

export default makeRouter;
