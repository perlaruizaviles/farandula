import React from "react";
import {Input, Search} from "semantic-ui-react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


const renderFieldWithError = (input, label, type, touched, error) => (
  <div className={(error && touched)? "error field": "field"}>
    <label>{label} {(error && touched)? `- ${error}`:""}</label>
    <div className="ui input">
      <input {...input} placeholder={label} type={type}/>
    </div>
  </div>
);


const renderSelectFieldWithError = ( input, label, type, touched, error, children ) => (
  <div className={(error && touched)? "error field": "field"}>
    <label>{label} {(error && touched)? `- ${error}`:""}</label>
    <div className="ui input">
      <select {...input}>
        {children}
      </select>
    </div>
  </div>
);



export const renderField = ({input, label, type, meta: {touched, error}, children}) => {
  if(type === "select"){
    return renderSelectFieldWithError(input, label, type, touched, error, children);
  }
  return renderFieldWithError(input, label, type, touched, error);
};

export const renderSearch = ({input: {value, onChange}, meta: {error}, results, onSearchChange}) => (
  <div className={(error) ? "error field" : "field"}>
      <div className="ui input">
        <Search value={value.title}
                results={results}
                onResultSelect={(e, value) => onChange(value)}
                onMouseDown={(e) => onChange("")}
                onSearchChange={onSearchChange}/>
    </div>
  </div>
);

export const renderDatePicker = ({input: {value, onChange}, results, minDate, maxDate}) => (
  <div>
      <DatePicker
        customInput={<Input icon="calendar outline" style={{width: '150px', color: '#216ba5'}}/>}
        selected={value}
        minDate={minDate}
        maxDate={maxDate}
        placeholderText="Select date..."
        onChange={date => onChange(date)}/>
  </div>
);
