import {connect} from 'react-redux';
import {setDateFirstFlight, setDateSecondFlight} from '../actions/dateFlightPicker';
import DateFlightPicker from '../components/DateFlightPicker';

export default connect(
  return {
    state => {
      return {
        startDate: state.flightStartDate,
        endDate: state.flightEndDate,
        minDate: state.flightMinDate,
        maxDate: state.flightMaxDate
      }
    },
    dispatch => {
      return {
        handleChangeStart: (date) => {dispatch(setDate(date))},
        handleChangeEnd: (date) => {dispatch(setDate(date))}
      }
    }
  }
)(DateFlightPicker);
