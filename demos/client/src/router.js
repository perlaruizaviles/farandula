import React from 'react';
import {syncHistoryWithStore} from 'react-router-redux';
import {Router, Route, browserHistory, IndexRedirect} from 'react-router';

import Main from './components/Main';
import Summary from './components/Summary';
import ResultsPage from './components/ResultsPage';

const makeRouter = store => {
  let history = syncHistoryWithStore(browserHistory, store);
  return (
    <Router history={history}>
      <Route path="/" component={Main}>
        <IndexRedirect to="/resultsPage"/>
        <Route path="/resultsPage" component={ResultsPage}/>
        <Route path="/summary" component={Summary}/>
      </Route>
    </Router>
  )
};

export default makeRouter;
