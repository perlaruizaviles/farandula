import _ from 'lodash'
import React, { Component } from 'react'
import { Search } from 'semantic-ui-react'
import data from '../data/airports.json'
import{Grid, Header, Icon} from 'semantic-ui-react'

const source =  data.airports.map(e => { return {title:e.city + ' - ' + e.iata , description:e.name}});

export default class AirportField extends Component {

  constructor(props){
    super(props);
    this.state = {
      isLoading: false, 
      results: [], 
      travelFrom: '', 
      travelTo:''}
  }


  handleResultSelectFrom = (e, result) => this.setState({ travelFrom: result.title, results:[] });

  handleSearchChangeFrom = (e, travelFrom) => {
    this.setState({ isLoading: true, travelFrom });

    setTimeout(() => {
      const re = new RegExp(_.escapeRegExp(this.state.travelFrom), 'i');
      const isMatch = (result) => {
        if(this.state.travelTo !== ""){ return re.test(result.title) && !result.title.includes(this.state.travelTo);}
        return re.test(result.title);
      };
      
      this.setState({
        isLoading: false,
        results: _.filter(source, isMatch).slice(0,5)
      })
    }, 500)
  };

  handleResultSelectTo = (e, result) => this.setState({ travelTo: result.title, results:[] });

  handleSearchChangeTo = (e, travelTo) => {
    this.setState({ isLoading: true, travelTo });

    setTimeout(() => {
      const re = new RegExp(_.escapeRegExp(this.state.travelTo), 'i');
      const isMatch = (result) => {
        if(this.state.travelFrom !== ""){ return re.test(result.title) && !result.title.includes(this.state.travelFrom);}
        return re.test(result.title);
      };
     this.setState({
        isLoading: false,
        results: _.filter(source, isMatch).slice(0,5)
      })
    }, 500)
  };


  render() {
    const { isLoading, travelFrom, travelTo, results } = this.state;

    return (
      <Grid>
        <Grid.Column width={4}>
          <Header>Flying from </Header>
          <Search
                loading={isLoading}
                onResultSelect={this.handleResultSelectFrom}
                onSearchChange={this.handleSearchChangeFrom}
                results={results}
                value={travelFrom}
                icon="plane"
                {...this.props}
              />
        </Grid.Column>
        <Grid.Column width={1}>
          <Icon name='exchange' size='big'/>
        </Grid.Column>
        <Grid.Column width={4}>
          <Header>Flying to </Header>
          <Search
                loading={isLoading}
                onResultSelect={this.handleResultSelectTo}
                onSearchChange={this.handleSearchChangeTo}
                results={results}
                value={travelTo}
                icon="plane"
                {...this.props}
              />
        </Grid.Column>
      </Grid>
    )
  }
}