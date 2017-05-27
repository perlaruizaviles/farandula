import {connect} from "react-redux";
import ItineraryList from "../components/Avail/ItineraryList";
import { changeOrderPrice } from "../actions/itineraryList";

export default connect(
  state => {
    return {
      travels: state.itineraries.get('itinerariesList'),
      order: state.itineraries.get('order')
    };
  },
  dispatch => {
    return {
      changeOrderPrice: (order) => dispatch(changeOrderPrice(order))
    };
  }
)(ItineraryList);