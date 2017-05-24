import {connect} from "react-redux";
import TravelSearch from "../components/Avail/TravelSearch";
import {
  cabinChange,
  changeTravelDate,
  changeTravelFrom,
  changeTravelTo,
  changeTravelType,
  cleanField,
  exchangeDestinations,
  searchAirport,
  searchAvailableFlights,
  travelerTypeCountChange
} from "../actions/travelConfig";

export default connect(
  state => {
    return {
      config: state.travelConfig,
      loading: state.ajaxCallsInProgress > 0
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
      availableFlights: (search) => dispatch(searchAvailableFlights(search)),
      cleanField: (quantum) => dispatch(cleanField(quantum))
    };
  }
)(TravelSearch);
