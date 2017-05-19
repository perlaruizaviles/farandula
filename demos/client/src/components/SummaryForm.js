import React from "react";
import {Field, reduxForm} from "redux-form";

const validate = values => {
  const errors = {};
  if (!values.name) {
    errors.name = 'Required'
  } else if (values.name.length > 15) {
    errors.name = 'Must be 15 characters or less'
  }
  if (!values.email) {
    errors.email = 'Required'
  } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
    errors.email = 'Invalid email address'
  }
  if (!values.age) {
    errors.age = 'Required'
  } else if (isNaN(Number(values.age))) {
    errors.age = 'Must be a number'
  } else if (Number(values.age) < 18) {
    errors.age = 'Sorry, you must be at least 18 years old'
  }
  return errors
};



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
        <Field
          name="name"
          type="text"
          component={renderField}
          label="Name"
        />
        <Field
          name="lastname"
          type="text"
          component={renderField}
          label="Lastname"
        />
      </div>
    </form>
  );
};

export default reduxForm({
  form: 'SummaryForm', // a unique identifier for this form
  validate, // <--- validation function given to redux-form
})(SummaryForm)

/*
const ContactForm = props => {
  const {handleSubmit, pristine, reset, submitting} = props;
  return (
    <form onSubmit={handleSubmit}>
      <Field
        name="username"
        type="text"
        component={renderField}
        label="Username"
      />
      <Field name="email" type="email" component={renderField} label="Email" />
      <Field name="age" type="number" component={renderField} label="Age" />
      <div>
        <button type="submit" disabled={submitting}>Submit</button>
        <button type="button" disabled={pristine || submitting} onClick={reset}>
          Clear Values
        </button>
      </div>
    </form>
  )
};

export default reduxForm({
  form: 'syncValidation', // a unique identifier for this form
  validate, // <--- validation function given to redux-form
  warn // <--- warning function given to redux-form
})(ContactForm)

 */