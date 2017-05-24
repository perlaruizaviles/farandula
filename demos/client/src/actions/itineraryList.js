import {CHANGE_ORDER} from './actionTypes';

export const changeOrder = order => {
    return{
        type: CHANGE_ORDER,
        value: order
    };
};