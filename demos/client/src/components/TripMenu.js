import React from 'react'
import { Menu } from 'semantic-ui-react'

//onClick={e => onCabinClick(id)}
const TripMenu = ({activeItem, handleItemClick}) => (
    <Menu text>
        <Menu.Item name='roundTrip' active={activeItem === 'roundTrip'} onClick={(e, {name}) => handleItemClick(name)} />
        <Menu.Item name='oneWay' active={activeItem === 'oneWay'} onClick={(e, {name}) => handleItemClick(name)} />
    </Menu>
)

// class TripMenu extends Component {
//   state = { activeItem: 'roundTrip' }
//
//   handleItemClick = (e, { name }) => this.setState({ activeItem: name })
//
//   render() {
//     const { activeItem } = this.state
//
//     return (
//       <Menu text>
//         <Menu.Item name='roundTrip' active={activeItem === 'roundTrip'} onClick={this.handleItemClick} />
//         <Menu.Item name='oneWay' active={activeItem === 'oneWay'} onClick={this.handleItemClick} />
//       </Menu>
//     )
//   }
// }

export default TripMenu;
