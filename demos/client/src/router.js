import React from 'react';
import {syncHistoryWithStore} from 'react-router-redux';
import {Router, Route, browserHistory, IndexRedirect} from 'react-router';

import Main from './components/Main';
import Home from './components/Home';
import Summary from './components/Summary';
import TravelResults from './components/TravelResults';

const makeRouter = store => {
  let history = syncHistoryWithStore(browserHistory, store);
  return (
    <Router history={history}>
      <Route path="/" component={Main}>
        <IndexRedirect to="/travelResults"/>
        <Route path="/home" component={Home}/>
        <Route path="/travelResults" component={TravelResults}/>
        <Route path="/summary" component={Summary}/>
      </Route>
    </Router>
  )
};

export default makeRouter;
