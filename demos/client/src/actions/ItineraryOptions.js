import {CHANGE_FILTER_LIMIT} from './actionTypes';

export const changeFilterLimit = limit => {
	return {
		type: CHANGE_FILTER_LIMIT,
		value: limit
	}
}