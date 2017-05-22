import React from "react";
import {Field, reduxForm} from "redux-form";
import {validate} from "../util/valitations";


const renderFieldWithoutError = (input, label, type) => (
  <div className="field">
    <label>{label}</label>
    <div className="ui input">
        <input {...input} placeholder={label} type={type} />
    </div>
  </div>
);

const renderFieldWithError = (input, label, type, touched, error) => (
  <div className="error field">
    <label>{label} - <i aria-hidden="true" className="red warning sign icon"/>{error}</label>
    <div className="ui input">
      <input {...input} placeholder={label} type={type} />
    </div>
  </div>
);


const renderField = ({input, label, type, meta: {touched, error}}) => {
  if (touched && (error)){
    return renderFieldWithError(input, label, type, touched, error);
  }
  return renderFieldWithoutError(input, label, type)
};


const SummaryForm = props =>{
  const {handleSubmit, pristine, reset, submitting} = props;

  return(
    <form onSubmit={handleSubmit} className="ui error form">
      <div className="equal width fields">
        <Field name="name" type="text" component={renderField} label="Name*"/>
        <Field name="lastname" type="text" component={renderField} label="Lastname*"/>
      </div>
      <div className="equal width fields">
        <Field name="middlename" type="text" component={renderField} label="Middle Name"/>
        <Field name="email" type="email" component={renderField} label="Email*"/>
      </div>
      <div className="equal width fields">
        <div className="field">
          <label>Gender</label>
          <Field name="gender" component="select">
            <option value="female">Female</option>
            <option value="male">Male</option>
          </Field>
        </div>
        <Field name="phone" type="tel" component={renderField} label="Phone #*"/>
        <Field name="age" type="number" component={renderField} label="Age*"/>
      </div>
      <div className="ui section divider">
      </div>
      <div>
        <button className="ui orange button" type="submit" disabled={submitting}>Book</button>
        <button className="ui grey basic button" type="button" disabled={pristine || submitting} onClick={reset}>
          Clear Values
        </button>
      </div>

    </form>
  );
};

export default reduxForm({
  form: 'SummaryForm', // a unique identifier for this form
  validate, // <--- validation function given to redux-form
})(SummaryForm)
