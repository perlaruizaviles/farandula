import {connect} from 'react-redux';
import FlightList from '../components/FlightList';

export default connect(
    state => {
        return {
            availableFlights: state.travelConfig.get('availableFlights')
        };
    },
    dispatch => {
        return {}
    }
)(FlightList);