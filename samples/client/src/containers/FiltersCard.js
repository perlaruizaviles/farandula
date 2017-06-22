import {connect} from "react-redux";
import FiltersCard from "../components/Avail/FiltersCard";
import { changeFilterLimit, changeAirlinesFilter } from "../actions/ItineraryOptions";

export default connect(
	state => {
		return {
			selectedLimit: state.itineraries.get('filters').get('limit'),
			airlines: state.itineraries.get('filters').get('airlines').keySeq().toArray()
		};
	},
	dispatch => {
		return {
			changeFilterLimit: (limit) => dispatch(changeFilterLimit(limit)),
			changeAirlinesFilter: (airline, selected) => dispatch(changeAirlinesFilter(airline, selected))
		};
	}
)(FiltersCard);