import React from 'react';
import DatePicker from 'react-datepicker';
import {Input} from 'semantic-ui-react';
import 'react-datepicker/dist/react-datepicker.css';

const DateSelector = ({minDate, maxDate, startDate, endDate, selected, changeTravelDate}) => (
    <DatePicker
        customInput={<Input icon="calendar outline" style={{color: '#216ba5'}}/>}
        minDate={minDate}
        maxDate={maxDate}
        selected={selected}
        startDate={startDate}
        endDate={endDate}
        onChange={(date) => changeTravelDate(date)}/>
);

export default DateSelector
