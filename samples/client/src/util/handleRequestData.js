import {getIata} from "./matcher";

const DEPARTING_AIRPORTS = "departingAirports";
const ARRIVAL_AIRPORTS = "arrivalAirports";
const DEPARTING_DATES = "departingDates";
const DEPARTING_TIMES = "departingTimes";
const globalDistributedSystems = ['amadeus', 'sabre', 'travelport'];

export function handleRequestData(values, type, passenger, cabin, limit) {
  const search = {
    type: type,
    passenger: passengerAdapter(passenger),
    cabin: cabin,
    limit: limit,
    gds: getRandomGDS(0, 3),
    departingAirportCodes: dataAdapter(DEPARTING_AIRPORTS, values),
    arrivalAirportCodes: dataAdapter(ARRIVAL_AIRPORTS, values),
    departingDates: dataAdapter(DEPARTING_DATES, values),
    departingTimes: dataAdapter(DEPARTING_TIMES, values)
  };

  if (values.arrivalDate) {
    search.returnDates = values.arrivalDate.format("YYYY-MM-DD");
    search.returnTimes = "00:00:00";
  }

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

function passengerAdapter(passenger) {
  return `children:${passenger.get('child')},infants:${passenger.get('lap-infant')},infantsOnSeat:${passenger.get('seat-infant')},adults:${passenger.get('adults')}`;
}

function getRandomGDS(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  const index = Math.floor(Math.random() * (max - min)) + min;
  return globalDistributedSystems[index];
}