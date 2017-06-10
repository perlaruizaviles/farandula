import React from "react";
import {shallow} from "enzyme";
import SearchForm from "../SearchForm";

describe('Rendering SearchForm ', () => {

  const tree = shallow( < SearchForm / >);

  it('Should create an snapshot for SearchForm', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders Correct SearchForm', () => {
    expect(tree.find('Connect').length).toBe(1);
  });
});