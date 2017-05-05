import _ from 'lodash'
import React from 'react'
import { Search } from 'semantic-ui-react'
import data from '../data/airports.json'
import{Grid,Header,Icon,Button} from 'semantic-ui-react'

const source =  data.airports.map(e => { return {title:e.city + ' - ' + e.iata , description:e.name}});

const AirportField = ({isLoading, travelFrom, travelTo, results,
                               handleResultSelectFrom, handleSearchChangeFrom,
                               handleResultSelectTo, handleSearchChangeTo,
                               handleExchange}) => {
  return (
    <Grid>
      <Grid.Column width={4}>
        <Header>Flying from </Header>
        <Search
          loading={isLoading}
          onResultSelect={(e, result) => handleResultSelectFrom(result.title, [])}
          onSearchChange={(e, travelFrom) => {
            handleSearchChangeFrom(true, travelFrom);
            setTimeout(() => {
              const re = new RegExp(_.escapeRegExp(travelFrom), 'i');
              const isMatch = (result) => {
                if(travelTo !== ""){ return re.test(result.title) && !result.title.includes(travelTo);}
                return re.test(result.title);
              };
              handleSearchChangeFrom(false, travelFrom);
              handleResultSelectFrom(travelFrom, _.filter(source, isMatch).slice(0, 5));
            }, 500)
          }}
          results={results}
          value={travelFrom}
          icon="plane"
        />
      </Grid.Column>
      <Grid.Column width={1}>
        <Button icon onClick={() => handleExchange(travelFrom, travelTo)}>
          <Icon name='exchange'/>
        </Button>
      </Grid.Column>
      <Grid.Column width={4}>
        <Header>Flying to </Header>
        <Search
          loading={isLoading}
          onResultSelect={(e, result) => handleResultSelectTo(result.title, [])}
          onSearchChange={(e, travelTo) => {
            handleSearchChangeTo(true, travelTo);
            setTimeout(() => {
              const re = new RegExp(_.escapeRegExp(travelTo), 'i');
              const isMatch = (result) => {
                if(travelFrom !== ""){ return re.test(result.title) && !result.title.includes(travelFrom);}
                return re.test(result.title);
              };
              handleSearchChangeTo(false, travelTo);
              handleResultSelectTo(travelTo, _.filter(source, isMatch).slice(0, 5));
            }, 500)
          }}
          results={results}
          value={travelTo}
          icon="plane"
        />
      </Grid.Column>
    </Grid>
  );
};

export default AirportField;

/*
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

  handleExchange = () => this.setState({travelFrom: this.state.travelTo, travelTo: this.state.travelFrom});


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
              />
        </Grid.Column>
        <Grid.Column width={1}>
            <Button icon onClick={this.handleExchange}>
              <Icon name='exchange' />
            </Button>
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
              />
        </Grid.Column>
      </Grid>
    )
  }
}
  */