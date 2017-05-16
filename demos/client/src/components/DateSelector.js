import React from 'react';
import DatePicker from 'react-datepicker';
import {Input} from 'semantic-ui-react';
import 'react-datepicker/dist/react-datepicker.css';
import PropTypes from 'prop-types';


const DateSelector = ({minDate, maxDate, startDate, endDate, selected, changeTravelDate}) => (
    <DatePicker
        customInput={<Input icon="calendar outline" style={{width:'150px', color: '#216ba5'}}/>}
        minDate={minDate}
        maxDate={maxDate}
        selected={selected}
        startDate={startDate}
        endDate={endDate}
        placeholderText="Select date..."
        onChange={(date) => changeTravelDate(date)}/>
);

DateSelector.propTypes = {
  minDate: PropTypes.object.isRequired,
  maxDate: PropTypes.object.isRequired,
  startDate: PropTypes.object,
  endDate: PropTypes.object,
  selected: PropTypes.object,
  changeTravelDate: PropTypes.func.isRequired
};

export default DateSelector;
