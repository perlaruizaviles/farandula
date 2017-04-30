import React, { Component } from 'react'
import DatePicker from 'react-datepicker'
import moment from 'moment';
import { Input } from 'semantic-ui-react'

import 'react-datepicker/dist/react-datepicker.css';

/*status:     startDate
*             endDate
*             maxDate
*
*functions:   handleChangeStart(dato)
*             handleChangeEnd
*/

const DateFlightPicker = ({startDate, endDate, maxDate, handleChangeEnd, handleChangeStart}) => (
  <div className="ui labeled input">
      <DatePicker
      customInput={<Input icon="calendar outline" style={{color: '#216ba5'}}/>}

          minDate={moment()}
          maxDate={maxDate}
          selected={startDate}
          selectsStart
          startDate={startDate}
          endDate={endDate}
          onChange={handleChangeStart.bind(this)}
      />
      <DatePicker
          customInput={<Input icon="calendar outline" style={{color: '#216ba5'}}/>}
          minDate={startDate}
          maxDate={state.maxDate}
          selected={endDate}
          selectsEnd
          startDate={startDate}
          endDate={endDate}
          onChange={handleChangeEnd.bind(this)}
      />
  </div>
);

// class DateFlightPicker extends Component {
//   constructor(){
//     super();
//     this.state = {
//       startDate: moment(),
//       endDate: moment(),
//       maxDate: moment().add(1,"year")
//     };
//   }
//
//   handleChangeStart(date) {
//     this.setState({
//     startDate: date
//     });
//     if(date>this.state.endDate){this.setState({endDate:date})}
//   }
//
//   handleChangeEnd(date) {
//     this.setState({
//     endDate: date
//     });
//   }
//
//   render(){
//     return(
//       <div className="ui labeled input">
//           <DatePicker
//           customInput={<Input icon="calendar outline" style={{color: '#216ba5'}}/>}
//
//               minDate={moment()}
//               maxDate={this.state.maxDate}
//               selected={this.state.startDate}
//               selectsStart
//               startDate={this.state.startDate}
//               endDate={this.state.endDate}
//               onChange={this.handleChangeStart.bind(this)}
//           />
//           <DatePicker
//               customInput={<Input icon="calendar outline" style={{color: '#216ba5'}}/>}
//               minDate={this.state.startDate}
//               maxDate={this.state.maxDate}
//               selected={this.state.endDate}
//               selectsEnd
//               startDate={this.state.startDate}
//               endDate={this.state.endDate}
//               onChange={this.handleChangeEnd.bind(this)}
//           />
//       </div>
//     )
//   }
// }

export default DateFlightPicker;
