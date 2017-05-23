import React from "react";
import {Field, FormSection} from "redux-form";
import {renderField} from "../../util/formFields";
import * as validate from "../../util/valitations";
import travelOptions from "../../data/travelOptions";

class BillingForm extends React.Component {
  render() {
    return <FormSection name="billing">
      <div className="equal width fields">
        <Field name="street1" type="text" component={renderField} label="Street 1*"
               validate={[validate.required, validate.minLength5]}/>
        <Field name="street2" type="text" component={renderField} label="Street 2"/>
      </div>
      <div className="equal width fields">
        <Field name="zip" type="number" component={renderField} label="Postal/Zip code*"
               validate={[validate.required, validate.zip]}/>
        <Field name="city" type="text" component={renderField} label="City*"
               validate={[validate.required, validate.alphaNum, validate.minLength2]}/>
      </div>
      <div className="equal width fields">
        <div className="field">
          <label>State/Region</label>
          <Field name="state" component="select" validate={validate.required}>
            <option />
            {travelOptions.get('states').map((state) =>
              <option key={state.value} value={state.value}>{state.text}</option>
            )}
          </Field>
        </div>
      </div>
      <div className="ui section divider">
      </div>

      <h3>Card Details</h3>
      <div className="equal width fields">
        <Field name="nameOnCard" type="text" component={renderField} label="Name on card*"
               validate={[validate.required, validate.alphaNum, validate.minLength5]}/>
        <Field name="cardNumber" type="number" component={renderField} label="Credit card #*"
               validate={[validate.required, validate.creditCard]}/>
      </div>
      <div className="equal width fields">
        <div className="field">
          <label>Month</label>
          <Field name="month" component="select" validate={validate.required}>
            <option />
            {travelOptions.get('months').map((month) =>
              <option key={month.value} value={month.value}>{month.text}</option>
            )}
          </Field>
        </div>
        <div className="field">
          <label>Year</label>
          <Field name="year" component="select" validate={validate.required}>
            <option />
            {travelOptions.get('years').map((year) =>
              <option key={year.value} value={year.value}>{year.text}</option>
            )}
          </Field>
        </div>
        <Field name="securityCode" type="number" component={renderField} label="Security code*"
               validate={[validate.required, validate.securityCode]}/>
      </div>

    </FormSection>
  }
}

export default BillingForm;