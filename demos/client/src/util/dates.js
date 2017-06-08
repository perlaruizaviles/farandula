import moment from "moment";

export const diffFormatted = (departureDate, arrivalDate) => {
  let totalHours = moment.unix(arrivalDate).diff(moment.unix(departureDate), 'hours');
  let clearMinutes = moment.unix(arrivalDate).diff(moment.unix(departureDate), 'minutes') % 60;

  return totalHours + "hr " + clearMinutes + "m";
};

export const formatTime = (Time) => {
  let timeFormatted = moment.unix(Time);
  return timeFormatted.format('h:mmp');
};
