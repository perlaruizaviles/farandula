import React from "react";
import {shallow} from "enzyme";
import TextMenu from "../Common/TextMenu";
import {List} from "immutable";

function setup() {
  const props = {
    options: List(['round', 'oneWay']),
    selected: 'round',
    selectType: () => {
    }
  };
  return shallow(<TextMenu {...props} />);
}

describe('Rendering TextMenu ', () => {
  it('Renders TextMenu', () => {
    const wrapper = setup();
    expect(wrapper.find('Menu').length).toBe(1);
  });
});