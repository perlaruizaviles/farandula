import React from 'react';
import {Provider} from 'react-redux';

import store from './store';
import makeRouter from './router';

const router = makeRouter(store);

const App = () => (
  <Provider>
    {router}
  </Provider>
);

export default App;
