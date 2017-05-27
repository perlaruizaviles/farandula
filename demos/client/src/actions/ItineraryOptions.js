import {CHANGE_FILTER_LIMIT, CHANGE_AIRLINES_FILTER} from './actionTypes';

export const changeFilterLimit = limit => {
		return {
			type: CHANGE_FILTER_LIMIT, 
			value: limit
		}
}

export const changeAirlinesFilter = (airline, selected) => {
		return {
			type: CHANGE_AIRLINES_FILTER,
			airline: airline, 
			selected: selected
		}
}