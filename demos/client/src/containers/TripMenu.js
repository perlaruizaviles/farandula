import {connect} from 'react-redux';
import {setTrip} from '../actions/activeTripItem';
import TripMenu from '../components/TripMenu';

export default connect(
    state => {
        return {
            activeItem: state.activeTripItem
        }
    },
    dispatch => {
        return {
            handleItemClick: (tripType) => {dispatch(setTrip(tripType))}
        }
    }
)(TripMenu);
