import React from "react";
import {Field, reduxForm} from "redux-form";
import {Input, Search} from "semantic-ui-react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


const renderSearch = ({input: {value, onChange}, label, results}) => (
  <div>
    <div>
      <label>{label}</label>
      <div className="ui input">
        <Search value={value.title} results={results} onResultSelect={(e, value) => onChange(value)}/>
      </div>
    </div>
  </div>
);

const renderDatepicker = ({input: {value, onChange}, label, results}) => (
  <div>
    <div>
      <label>{label}</label>
      <DatePicker
        customInput={<Input icon="calendar outline" style={{width: '150px', color: '#216ba5'}}/>}
        selected={value}
        placeholderText="Select date..."
        onChange={date => onChange(date)}/>
    </div>
  </div>
);

const SearchForm = props => {
  const {handleSubmit, pristine, reset, submitting} = props;

  return (
    <form onSubmit={handleSubmit} className="ui error form">
      <div className="equal width fields">
        <Field name="Airports" type="text" component={renderSearch} label="Airports*"
               results={[{title: 'asd', description: 'asdasd'}]}/>

        <Field name="Date" type="text" component={renderDatepicker} label="Date*"/>
      </div>

      <div className="ui section divider">
      </div>

      <div>
        <button className="ui orange button" type="submit" disabled={submitting}>Book</button>
        <button className="ui grey basic button" type="button" disabled={pristine || submitting} onClick={reset}>
          Clear Form
        </button>
      </div>
    </form>
  );
};

export default reduxForm({
  form: 'SearchForm', // a unique identifier for this form
})(SearchForm)
