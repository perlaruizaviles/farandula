import {connect} from 'react-redux';
import TravelSearch from '../components/TravelSearch';
import {changeTravelType, changeTravelDate, travelerTypeCountChange, cabinChange, changeTravelFrom, changeTravelTo, searchAirport, exchangeDestinations, searchAvailableFlights} from '../actions/travelConfig';

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
      cabinChange: (cabin) => dispatch(cabinChange(cabin)),
      fromAirportChange: (airport) => dispatch(changeTravelFrom(airport)),
      toAirportChange: (airport) => dispatch(changeTravelTo(airport)),
      searchAirport: (query, quantum) => dispatch(searchAirport(query, quantum)),
      exchangeDestinations: (from, to) => dispatch(exchangeDestinations(from, to)),
      availableFlights: (e, departureAirport, departingDate, departingTime, arrivalAirport, arrivalDate, arrivalTime, type, passenger) => dispatch(searchAvailableFlights(e,departureAirport, departingDate, departingTime, arrivalAirport, arrivalDate, arrivalTime, type, passenger))
    }
  }
)(TravelSearch);
