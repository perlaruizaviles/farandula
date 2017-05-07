import {connect} from 'react-redux';
import TravelSearch from '../components/TravelSearch';
import {changeTravelType, changeTravelDate, travelerTypeCountChange, cabinChange} from '../actions/travelConfig';

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
      travelerTypeCountChange: (TravelerType, count) => dispatch(travelerTypeCountChange(TravelerType, count)),
      cabinChange: (cabin) => dispatch(cabinChange(cabin))
    }
  }
)(TravelSearch);
