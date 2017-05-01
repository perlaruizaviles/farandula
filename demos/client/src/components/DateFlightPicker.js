import React from 'react'
import DatePicker from 'react-datepicker'
import { Input } from 'semantic-ui-react'
import 'react-datepicker/dist/react-datepicker.css';

const DateFlightPicker = ({startDate, endDate, handleChangeStart, handleChangeEnd, minDate, maxDate}) => {
  return (
    <div>
    <DatePicker
      customInput={<Input icon="calendar outline" style={{color: '#216ba5'}}/>}
      minDate={minDate}
      maxDate={maxDate}
      selected={startDate}
      selectsStart
      startDate={startDate}
      endDate={endDate}
      onChange={date => {
        handleChangeStart(date);
        if (date > endDate)
          handleChangeEnd(date)
      }}
    />
    <DatePicker
      customInput={<Input icon="calendar outline" style={{color: '#216ba5'}}/>}
      minDate={startDate}
      maxDate={maxDate}
      selected={endDate}
      selectsStart
      startDate={startDate}
      endDate={endDate}
      onChange={handleChangeEnd}
    />
    </div>
  )
};

export default DateFlightPicker;
