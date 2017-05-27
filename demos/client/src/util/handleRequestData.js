import {getIata} from "./matcher";


export function handleRequestData(values, type, passenger, cabin) {
  const search = {
    type: type,
    passenger: passenger,
    cabin: cabin
  };

  search.departingAirports = departingAirports(values);
  search.arrivalAirports = arrivalAirports(values);
  search.departingDates = departureDates(values);

  return search;
}


function departingAirports(values) {
  let iatas = "";
  for (let destiny in values) {
    if (values.hasOwnProperty(destiny)) {
      iatas += "," + getIata(values[destiny].departingAirport.title);
    }
  }
  return iatas.slice(1,iatas.length);
}

function arrivalAirports(values) {
  let iatas = "";
  for (let destiny in values) {
    if (values.hasOwnProperty(destiny)) {
      iatas += "," + getIata(values[destiny].arrivalAirport.title);
    }
  }
  return iatas.slice(1,iatas.length);
}

function departureDates(values) {
  let dates = "";
  for (let destiny in values) {
    if (values.hasOwnProperty(destiny)) {
      dates += "," + values[destiny].departingDate.format("YYYY-MM-DD");
    }
  }
  return dates.slice(1,dates.length);
}