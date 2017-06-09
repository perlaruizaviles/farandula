import * as actions from "../itineraryList";

describe('actions', () => {

  describe('orderPriceAsc', function () {

    it('should have a type of ORDER_PRICE_ASC', function () {
      expect(actions.orderPriceAsc().type).toEqual('ORDER_PRICE_ASC');
    });
  });

  describe('orderPriceDesc', function () {

    it('should have a type of ORDER_PRICE_DESC', function () {
      expect(actions.orderPriceDesc().type).toEqual('ORDER_PRICE_DESC');
    });
  });
});