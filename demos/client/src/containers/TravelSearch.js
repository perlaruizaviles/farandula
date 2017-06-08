import {connect} from "react-redux";
import TravelSearch from "../components/Avail/TravelSearch";
import {
    addDestiny,
    cabinChange,
    changeTravelerCount,
    changeTravelType,
    cleanField,
    removeDestiny,
    searchAirport,
    searchAvailableFlights
} from "../actions/travelConfig";

export default connect(
  state => {
    return {
      config: state.travelConfig,
      filters: state.itineraries,
      loading: state.ajaxCallsInProgress > 0
    };
  },
  dispatch => {
    return {
      actions:{
        typeChange: (type) => dispatch(changeTravelType(type)),
        travelerTypeCountChange: (TravelerType, value, count, totalTravelers) => dispatch(changeTravelerCount(TravelerType, value, count, totalTravelers)),
        cabinChange: (cabin) => dispatch(cabinChange(cabin)),
        searchAirport: (query, quantum) => dispatch(searchAirport(query, quantum)),
        availableFlights: (search) => dispatch(searchAvailableFlights(search)),
        cleanField: (quantum) => dispatch(cleanField(quantum)),
        addDestiny: () => dispatch(addDestiny()),
        removeDestiny: () => dispatch(removeDestiny())
      }
    };
  }
)(TravelSearch);
