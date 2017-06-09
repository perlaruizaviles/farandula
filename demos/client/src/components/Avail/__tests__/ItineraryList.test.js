import {shallow} from "enzyme";
import ItineraryList from "../ItineraryList";

describe('Rendering ItineraryList ', () => {

  const tree = shallow( < ItineraryList / >);

  it('Should create an snapshot for ItineraryList', () => {
    expect(tree).toMatchSnapshot();
  });

  it('Renders Correct BillingForm', () => {
    expect(tree.find('Segment').length).toBe(1);
  });
});