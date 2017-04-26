import React, { Component } from 'react';
import AirportFieldDemo from './components/AirportField.demo.js';

class App extends Component {
 render() {
   return (
    <div>
       <div>
        <h2>Welcome to Quantum Show Business</h2>
      </div>
      <p>
        This is a <strong>work in progress</strong>.
      </p>
      
      <AirportFieldDemo/>
    </div>
   );
 }
}

export default App;
