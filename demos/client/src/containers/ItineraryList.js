import {connect} from "react-redux";
import ItineraryList from "../components/Avail/ItineraryList";
import { changeOrder } from "../actions/itineraryList";

export default connect(
  state => {
    return {
      travels: state.travelConfig.get('availableFlights'),
      order: state.travelConfig.get('order')
    };
  },
  dispatch => {
    return {
      changeOrder: (order) => dispatch(changeOrder(order))
    }
  }
)(ItineraryList);