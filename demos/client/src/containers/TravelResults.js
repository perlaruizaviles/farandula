import {connect} from 'react-redux';
import TravelResults from '../components/TravelResults';

export default connect(
    state => {
        return {
            travels: state.travelConfig.get('availableFlights')
        };
    },
    dispatch => {
        return {}
    }
)(TravelResults);