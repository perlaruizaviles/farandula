import {Map, List} from "immutable";

const ItineraryOptions = Map({
  order: List(['price-low-to-high', 'price-high-to-low']),
  filters: Map({
    limits: List(['25', '50', '100']),
    airlines: Map(
      {'aeromexico': false,
       'interjet': false}),
      )
  })
});

export default ItineraryOptions;