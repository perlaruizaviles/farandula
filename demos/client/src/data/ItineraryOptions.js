import {Map, List} from "immutable";

const ItineraryOptions = Map({
  order: List(['price-low-to-high', 'price-high-to-low']),
  filters: Map({
    limits: List(['25', '50', '100'])
  })
});

export default ItineraryOptions;