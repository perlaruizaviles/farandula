import React, { Component } from 'react';
import Airportfield from './components/airportfield/airportfield.js';
import{Grid, Header, Icon} from 'semantic-ui-react';

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
      
      <Grid>
        <Grid.Column width={4}>
          <Header>Flying from </Header>
          <Airportfield />
        </Grid.Column>
        <Grid.Column width={1}>
          <Icon name='exchange' size='big'/>
        </Grid.Column>
        <Grid.Column width={4}>
          <Header>Flying to </Header>
          <Airportfield />
        </Grid.Column>       
      </Grid>
    </div>
   );
 }
}

export default App;
