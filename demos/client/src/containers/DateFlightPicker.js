import {connect} from 'react-redux';
import {setDateFirstFlight, setDateSecondFlight} from '../actions/dateFlightPicker';
import DateFlightPicker from '../components/DateFlightPicker';

export default connect(
  state => {
    return {
      startDate: state.startDate,
      endDate: state.endDate,
      minDate: state.minDate,
      maxDate: state.maxDate
    }
  },
  dispatch => {
    return {
      handleChangeStart: (date) => {dispatch(setDateFirstFlight(date))},
      handleChangeEnd: (date) => {dispatch(setDateSecondFlight(date))}
    }
  }
)(DateFlightPicker);
