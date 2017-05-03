import {connect} from 'react-redux';
import TravelSearch from '../components/TravelSearch';
import {
  changeTravelType,
  changeTravelDate,
  changeDateReturn} from '../actions/travelConfig';

export default connect(
  state => {
    return {
      config: state.travelConfig
    };
  },
  dispatch => {
    return {
      typeChange: (type) => dispatch(changeTravelType(type)),
      changeDateDepart: (date) => dispatch(changeTravelDate(date)),
      changeDateReturn: (date) => dispatch(changeDateReturn(date))
    }
  }
)(TravelSearch);
