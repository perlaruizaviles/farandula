import _ from 'lodash'
import faker from 'faker'
import React, { Component } from 'react'
import { Search } from 'semantic-ui-react'

const source = _.times(5, () => ({
  title: faker.address.city(),
  description: faker.address.country()
}));

export default class AirportField extends Component {
  componentWillMount() {
    this.resetComponent()
  }

  resetComponent = () => this.setState({ isLoading: false, results: [], value: '' });

  handleResultSelect = (e, result) => this.setState({ value: result.title });

  handleSearchChange = (e, value) => {
    this.setState({ isLoading: true, value });

    setTimeout(() => {
      if (this.state.value.length < 1) return this.resetComponent();

      const re = new RegExp(_.escapeRegExp(this.state.value), 'i');
      const isMatch = (result) => re.test(result.title);

      this.setState({
        isLoading: false,
        results: _.filter(source, isMatch),
      })
    }, 500)
  };

  render() {
    const { isLoading, value, results } = this.state;

    return (
          <Search
            loading={isLoading}
            onResultSelect={this.handleResultSelect}
            onSearchChange={this.handleSearchChange}
            results={results}
            value={value}
            icon="plane"
            {...this.props}
          />
    )
  }
}