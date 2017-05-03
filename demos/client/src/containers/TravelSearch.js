import {connect} from 'react-redux';
import TravelSearch from '../components/TravelSearch';
import {changeTravelType} from '../actions/travelConfig';

export default connect(
  state => {
    return {
      config: state.travelConfig
    };
  },
  dispatch => {
    return {
      typeChange: (type) => dispatch(changeTravelType(type))
    }
  }
)(TravelSearch);