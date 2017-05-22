import React from "react";
import {reduxForm} from "redux-form";
import PassengerForm from "./PassengerForm";
import BillingForm from "./BillingForm";


const SummaryForm = props =>{
  const {handleSubmit, pristine, reset, submitting} = props;

  return(
    <form onSubmit={handleSubmit} className="ui error form">
      <h3>Passenger(s)</h3>
      <PassengerForm name="passenger1" />
      <PassengerForm name="passenger2" />

      <h3>Billing Information</h3>
      <BillingForm/>
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
  form: 'SummaryForm', // a unique identifier for this form
})(SummaryForm)
