import {getIata} from "./matcher";

const DEPARTING_AIRPORTS = "departingAirports";
const ARRIVAL_AIRPORTS = "arrivalAirports";
const DEPARTING_DATES = "departingDates";
const DEPARTING_TIMES = "departingTimes";

export function handleRequestData(values, type, passenger, cabin, limit) {
  const search = {
    type: type,
    passenger: passenger,
    cabin: cabin,
    limit: limit
  };

  if (values.arrivalDate){
    search.returnDates = values.arrivalDate.format("YYYY-MM-DD");
    search.returnTimes = "00:00:00";
  }

  search.departingAirports = dataAdapter(DEPARTING_AIRPORTS, values);
  search.arrivalAirports = dataAdapter(ARRIVAL_AIRPORTS, values);
  search.departingDates = dataAdapter(DEPARTING_DATES, values);
  search.departingTimes = dataAdapter(DEPARTING_TIMES, values);

  return search;
}

function dataAdapter(key, values) {
  let properties = "";
  for (let destiny in values) {
    if (values.hasOwnProperty(destiny) && destiny !== "arrivalDate") {
      switch (key) {
        case DEPARTING_AIRPORTS:
          properties += "," + getIata(values[destiny].departingAirport.title);
          break;
        case ARRIVAL_AIRPORTS:
          properties += "," + getIata(values[destiny].arrivalAirport.title);
          break;
        case DEPARTING_DATES:
          properties += "," + values[destiny].departingDate.format("YYYY-MM-DD");
          break;
        case DEPARTING_TIMES:
          properties += ",00:00:00";
          break;
        default:
          return properties;
      }
    }
  }
  return properties.slice(1, properties.length);
}
