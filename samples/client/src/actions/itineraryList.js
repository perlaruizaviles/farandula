import * as types from "./actionTypes";

export const orderPriceAsc = () => {
  return {
    type: types.ORDER_PRICE_ASC
  }
};

export const orderPriceDesc = () => {
  return {
    type: types.ORDER_PRICE_DESC
  }
};

export const changeOrderPrice = order => {
  return (dispatch) => {
    if (order === 'price-high-to-low') {
      dispatch(orderPriceDesc());
    } else if (order === 'price-low-to-high') {
      dispatch(orderPriceAsc());
    }
  }
};