import React from "react";
import {render} from "enzyme";
import ItineraryListOptions from "../ItineraryListOptions";

describe('Rendering ItineraryListOptions ', () => {

  const tree = render( < ItineraryListOptions / >);

  it('Should create an snapshot for ItineraryListOptions', () => {
    expect(tree).toMatchSnapshot();
  });
});