import React from "react";
import {syncHistoryWithStore} from "react-router-redux";
import {browserHistory, IndexRedirect, Route, Router} from "react-router";
import * as routes from "./routes";

import Main from './components/Main';
import Summary from './components/Summary';
import ResultsPage from './components/ResultsPage';

const makeRouter = store => {
  let history = syncHistoryWithStore(browserHistory, store);
  return (
    <Router history={history}>
      <Route path="/" component={Main}>
        <IndexRedirect to={routes.HOME}/>
        <Route path={routes.HOME} component={ResultsPage}/>
        <Route path={routes.SUMMARY} component={Summary}/>
        <Route path={routes.RESULTS} component={ResultsPage}/>
      </Route>
    </Router>
  )
};

export default makeRouter;
