import * as types from "../actions/actionTypes";
import {Map} from "immutable";

const resultOptions = (state = Map({}), action) => {
    switch (action.type){
        case types.CHANGE_ORDER:
            return state.order.set(action.value);
        default:
            return state;
    }
}

export default resultOptions;