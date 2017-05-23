import {connect} from "react-redux";
import ItineraryList from "../components/Avail/ItineraryList";

export default connect(
  state => {
    return {
      travels: state.travelConfig.get('availableFlights'),
      order: state.travelConfig.get('order')
    };
  },
  dispatch => {
    return {}
  }
)(ItineraryList);