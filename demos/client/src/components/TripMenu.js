import React, { Component } from 'react'
import { Menu } from 'semantic-ui-react'

class TripMenu extends Component {
  state = { activeItem: 'roundTrip' }

  handleItemClick = (e, { name }) => this.setState({ activeItem: name })

  render() {
    const { activeItem } = this.state

    return (
      <Menu text>
        <Menu.Item name='roundTrip' active={activeItem === 'roundTrip'} onClick={this.handleItemClick} />
        <Menu.Item name='oneWay' active={activeItem === 'oneWay'} onClick={this.handleItemClick} />
      </Menu>
    )
  }
}

export default TripMenu;
