import React from "react";
import {mount, shallow} from "enzyme";
import App from "./App";

// Shallow smoke rendering test
it('renders without crashing (shallow)', () => {
  shallow(<App/>);
});

// Full smoke rendering test
it('renders without crashing (full)', () => {
  mount(<App/>);
});