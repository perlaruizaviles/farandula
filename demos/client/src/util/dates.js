import moment from "moment";
import {Map} from 'immutable';

export const diffFormatted = (departureDate, arrivalDate) => {
  let totalHours = moment.unix(arrivalDate).diff(moment.unix(departureDate), 'hours');
  let clearMinutes = moment.unix(arrivalDate).diff(moment.unix(departureDate), 'minutes') % 60;

  return totalHours + "hr " + clearMinutes + "m";
};

export const formatTime = (Time) => {
  let timeFormatted = moment.unix(Time);
  return timeFormatted.format('h:mmp');
};

export const changeRangeDate = (dateOne, dateTwo, shouldDateOneBeFirst = true) => {
  if (dateOne.diff(dateTwo) > 0){
    return (shouldDateOneBeFirst)? 
      Map({'depart':dateOne, 'return':dateOne}) : 
      Map({'depart':dateTwo, 'return':dateTwo})
  } else {
    return Map({'depart':dateOne, 'return':dateTwo});
  }
}

export const changeTravelDates = (dates, newDate, dateType) => {
  if(dates.get('depart') == null && dates.get('return') == null) return Map({'depart': newDate, 'return': newDate})
  if(dateType==='depart'){
    dates = changeRangeDate(newDate, dates.get('return'));
  } else if (dateType==='return'){
    dates = changeRangeDate(dates.get('depart'), newDate, false);
  }
  return dates;
}
