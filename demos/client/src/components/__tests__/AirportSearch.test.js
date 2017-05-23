import React from "react";
import {shallow} from "enzyme";
import expect from "expect";
import AirportSearch from "../Avail/AirportSearch";

function setup() {
  const props = {
    changeSelected: () => {
    },
    searchChange: () => {
    },
    airports: [{title: 'Mexicali - MXL', description: 'General Rodolfo Se1nchez Taboada International Airport'}],
    value: "Mexicali - MXL",
    cleanField: () => {
    }
  };
  return shallow(<AirportSearch {...props} />);
}

describe('Rendering AirportSearch', () => {
  it('Renders Search', () => {
    const wrapper = setup();
    expect(wrapper.find('Search').length).toBe(1);
  });

  it('Initial Value ', () => {
    const wrapper = setup();
    expect(wrapper.find('Search').props().value).toBe('Mexicali - MXL');
  });
});