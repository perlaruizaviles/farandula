import {connect} from 'react-redux';
import {setCabin, addPassenger, removePassenger} from '../actions/flightSettings';
import FlightOptionsMenu from '../components/FlightOptionsMenu';

export default connect(
    state => {
        return {
            activeItem: state.activeTripItem,
        }
    },
    dispatch => {
        return {
            handleItemClick: (id) => {dispatch(setCabin(id))}
            //(e, { name }) => this.setState({ activeItem: name })
        }
    }
)(FlightOptionsMenu);