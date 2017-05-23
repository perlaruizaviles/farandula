import moment from "moment";

export function nYearsFromNow(n) {
  let years = [];
  for (let i =0; i<=n; i++){
    years.push({text:moment().add(i,"year").format('YYYY'), value:moment().add(i,"year").format('YYYY')})
  }
  return years;
}