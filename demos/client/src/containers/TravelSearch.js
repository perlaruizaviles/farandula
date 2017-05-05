import {connect} from 'react-redux';
import TravelSearch from '../components/TravelSearch';
import {changeTravelType, changeTravelDate, changeTravelFrom, changeTravelTo, loadAirports} from '../actions/travelConfig';

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
      fromAirportChange: (airport) => dispatch(changeTravelFrom(airport)),
      toAirportChange: (airport) => dispatch(changeTravelTo(airport)),
      loadAirports:() => dispatch(loadAirports())
    }
  }
)(TravelSearch);
