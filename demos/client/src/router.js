import React from "react";
import {syncHistoryWithStore} from "react-router-redux";
import {browserHistory, IndexRedirect, Route, Router} from "react-router";

import Main from "./components/Main";
import Home from "./components/Home";
import Summary from "./components/Summary";
import TravelResults from "./components/TravelResults";
import ContactPage from "./components/ContactPage";

const makeRouter = store => {
  let history = syncHistoryWithStore(browserHistory, store);
  return (
    <Router history={history}>
      <Route path="/" component={Main}>
        <IndexRedirect to="/travelResults"/>
        <Route path="/home" component={Home}/>
        <Route path="/travelResults" component={TravelResults}/>
        <Route path="/summary" component={Summary}/>
        <Route path="/contact" component={ContactPage}/>
      </Route>
    </Router>
  )
};

export default makeRouter;
