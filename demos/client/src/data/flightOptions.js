import {Map, List} from 'immutable';

const flightOptions = Map({
  cabin: List([
    Map({
      id: 'economy',
      name: 'economy'
    }),
    Map({
      id: 'premium-economy',
      name: 'premium economy'
    }),
    Map({
      id: 'business',
      name: 'business'
    }),
    Map({
      id: 'first',
      name: 'first'
    })
  ]),
  passengers: List([
    Map({
      id: 'adult',
      name: 'adult',
      ageRange: List([18, 64])
    }),
    Map({
      id: 'senior',
      name: 'senior',
      ageRange: List([65, Infinity])
    }),
    Map({
      id: 'youth',
      name: 'youth',
      ageRange: List([12, 17])
    }),
    Map({
      id: 'child',
      name: 'child',
      ageRange: List([2, 11])
    }),
    Map({
      id: 'seat-infant',
      name: 'seat infant',
      ageRange: List([0, 2])
    }),
    Map({
      id: 'lap-infant',
      name: 'lap infant',
      ageRange: List([0, 2])
    })
  ])
});

export default flightOptions;