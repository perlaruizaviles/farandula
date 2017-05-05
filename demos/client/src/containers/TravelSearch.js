import {connect} from 'react-redux';
import TravelSearch from '../components/TravelSearch';
import {changeTravelType, changeTravelDate, increasePassanger, decreasePassanger} from '../actions/travelConfig';

export default connect(
  state => {
    return {
      config: state.travelConfig
    };
  },
  dispatch => {
    return {
      typeChange: (type) => dispatch(changeTravelType(type)),
      dateChange: (dateType, date) => dispatch(changeTravelDate(dateType, date)),
      increasePassanger: (passangerType, count) => dispatch(increasePassanger(passangerType, count)),
      decreasePassanger: (passangerType, count) => dispatch(decreasePassanger(passangerType, count))
    }
  }
)(TravelSearch);
