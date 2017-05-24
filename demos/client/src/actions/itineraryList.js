import * as types from './actionTypes';

export const orderPriceAsc = () => {
  return {
    type: types.ORDER_PRICE_ASC
  }
}

export const orderPriceDesc = () => {
  return {
    type: types.ORDER_PRICE_DESC
  }
}

export const changeOrderPrice = order => {
  return {
    type: types.CHANGE_PRICE_ORDER,
    order: order
  }
};