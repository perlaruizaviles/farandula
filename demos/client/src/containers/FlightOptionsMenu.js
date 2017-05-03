import {connect} from 'react-redux';
import {setCabin, addPassenger, removePassenger} from '../actions/flightSettings';
import FlightOptionsMenu from '../components/FlightOptionsMenu';

export default connect(
  state => {
    return {
      settings: state.flightSettings,
      options: state.flightOptions
    }
  },
  dispatch => {
    return {
      onCabinClick: (id) => {dispatch(setCabin(id))},
      onMorePassengerClick: (id) => {dispatch(addPassenger(id))},
      onLessPassengerClick: (id) => {dispatch(removePassenger(id))}
    }
  }
)(FlightOptionsMenu);