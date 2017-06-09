import * as actions from "../ItineraryOptions";

describe('actions', () => {

  describe('changeFilterLimit', function () {

    const limit = 50;

    it('should have a type of CHANGE_FILTER_LIMIT', function () {
      expect(actions.changeFilterLimit().type).toEqual('CHANGE_FILTER_LIMIT');
    });

    it('should pass on the limit we pass in', function () {
      expect(actions.changeFilterLimit(limit).limit).toEqual(limit);
    });
  });

  describe('changeAirlinesFilter', function () {

    const airline = 'aeromexico';
    const selected = undefined;

    it('should have a type of CHANGE_AIRLINES_FILTER', function () {
      expect(actions.changeAirlinesFilter().type).toEqual('CHANGE_AIRLINES_FILTER');
    });

    it('should pass on the airline we pass in', function () {
      expect(actions.changeAirlinesFilter(airline).airline).toEqual(airline);
    });

    it('should pass on the selected we pass in', function () {
      expect(actions.changeAirlinesFilter(selected).selected).toEqual(selected);
    });
  });
});