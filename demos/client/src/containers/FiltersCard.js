import {connect} from "react-redux";
import FiltersCard from "../components/Avail/FiltersCard";
import { changeFilterLimit } from "../actions/ItineraryOptions";

export default connect(
	state => {
		return {
			selectedLimit: state.travelConfig.get('filters').get('limit')
		};
	},
	dispatch => {
		return {
			changeFilterLimit: (limit) => dispatch(changeFilterLimit(limit))
		};
	}
)(FiltersCard);