import React from "react";
import {reduxForm} from "redux-form";
import PassengerForm from "./PassengerForm";


const SummaryForm = props =>{
  const {handleSubmit, pristine, reset, submitting} = props;

  return(
    <form onSubmit={handleSubmit} className="ui error form">
      <PassengerForm name="passenger1" />
      <PassengerForm name="passenger2" />
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
})(SummaryForm)
