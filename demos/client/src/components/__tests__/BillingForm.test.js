import React from "react";
import {shallow} from "enzyme";
import expect from "expect";
import BillingForm from "../BillingForm";

function setup() {
  const props = {};
  return shallow(<BillingForm {...props} />);
}

describe('Rendering BillingForm', () => {
  it('Renders Form', () => {
    const wrapper = setup();
    expect(wrapper.find('FormSection').length).toBe(1);
  });

  it('Initial Inputs ', () => {
    const wrapper = setup();
    expect(wrapper.find('Field').length).toBe(10);
  });
});