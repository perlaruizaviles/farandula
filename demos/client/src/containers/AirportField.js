import {connect} from 'react-redux';
import AirportField from '../components/AirportField';
import {setTravelFrom, setTravelTo, setLoading, setResults} from '../actions/airportField';

export default connect(
  state => {
    return {
      isLoading: state.isLoading,
      travelFrom: state.travelFrom,
      travelTo: state.travelTo,
      results: state.results
    };
  },
  dispatch => {
    return {
      handleResultSelectFrom: (title, results) => {
        dispatch(setTravelFrom(title));
        dispatch(setResults(results));
      },
      handleSearchChangeFrom: (isLoading, title) => {
        dispatch(setLoading(isLoading));
        dispatch(setTravelFrom(title));
      },
      handleResultSelectTo: (title, results) => {
        dispatch(setTravelTo(title));
        dispatch(setResults(results));
      },
      handleSearchChangeTo: (isLoading, title) => {
        dispatch(setLoading(isLoading));
        dispatch(setTravelTo(title));
      },
    };
  })(AirportField);