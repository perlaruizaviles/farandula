import {List, Map} from "immutable";
import moment from "moment";
import {nYearsFromNow} from "../util/nYearsFromNow";
import states from "../util/states.json";

const travelOptions = Map({
  type: List(['round', 'oneWay', 'multiCity']),
  cabin: List(['economy', 'premiumEconomy', 'business', 'first']),
  minDate: moment(),
  maxDate: moment().add(1, "year"),
  travelers: Map({
    'adults': List([18, Infinity]),
    'child': List([2, 11]),
    'seat-infant': List([0, 2]),
    'lap-infant': List([0, 2])
  }),
  locations: Map({
    ids: List(['from', 'to']),
    fields: List(['iata', 'city', 'name'])
  }),
  dates: Map({
    'round': List(['depart', 'return']),
    'one-way': List(['depart']),
    'multi-city': List(['depart-n'])
  }),
  months: [
    {text: 'January', value: '01'},
    {text: 'February', value: '02'},
    {text: 'March', value: '03'},
    {text: 'April', value: '04'},
    {text: 'May', value: '05'},
    {text: 'June', value: '06'},
    {text: 'July', value: '07'},
    {text: 'August', value: '08'},
    {text: 'September', value: '09'},
    {text: 'October', value: '10'},
    {text: 'November', value: '11'},
    {text: 'December', value: '12'},
  ],
  years: nYearsFromNow(10),
  states: states,
  genders: [
    {text: 'Male', value: '1'},
    {text: 'Female', value: '2'}
  ]
});

export default travelOptions;
