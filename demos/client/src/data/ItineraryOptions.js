import {Map, List} from "immutable";

const ItineraryOptions = Map({
  order: List(['price-low-to-high', 'price-high-to-low']),
  filters: Map({
    limits: List(['25', '50', '100']),
    airlines: List([Map({name: 'aeromexico', selected: false}), Map({name: 'interjet', selected: false})])
  })
});

export default ItineraryOptions;