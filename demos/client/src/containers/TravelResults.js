import {connect} from 'react-redux';
import TravelResults from '../components/TravelResults';

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
)(TravelResults);