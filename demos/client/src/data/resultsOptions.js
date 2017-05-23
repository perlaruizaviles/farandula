import {Map, List} from 'immutable';
import moment from 'moment';

const resultsOptions = Map({
    order: Map(
        {id:'price-low-first', description:'Price: Low to High'},
        {id:'price-high-first', description:'Price: High to Low'}
        )
});

export default resultsOptions;