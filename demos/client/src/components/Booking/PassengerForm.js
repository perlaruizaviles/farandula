import React from "react";
import {Field, FormSection} from "redux-form";
import {renderField} from "../../util/formFields";
import * as validate from "../../util/valitations";
import travelOptions from "../../data/travelOptions";

class PassengerForm extends React.Component {
  render() {
    return <FormSection name={this.props.name}>
      <div className="equal width fields">
        <Field name="name" type="text" component={renderField} label="Name*"
               validate={[validate.required, validate.maxLength15, validate.minLength2, validate.alphaNum]}/>
        <Field name="lastname" type="text" component={renderField} label="Lastname*"
               validate={[validate.required, validate.maxLength15, validate.minLength2, validate.alphaNum]}/>
      </div>
      <div className="equal width fields">
        <Field name="middlename" type="text" component={renderField} label="Middle Name"
               validate={[validate.maxLength15, validate.minLength5, validate.alphaNum]}/>
        <Field name="email" type="email" component={renderField} label="Email*"
               validate={[validate.required, validate.email, validate.aol]}/>
      </div>
      <div className="equal width fields">
        <div className="field">
          <Field name="gender" component={renderField} type="select" label="Gender*" validate={validate.required}>
            <option />
            {travelOptions.get('genders').map((gender) =>
              <option key={gender.value} value={gender.value}>{gender.text}</option>
            )}
          </Field>
        </div>
        <Field name="phone" type="tel" component={renderField} label="Phone #*"
               validate={[validate.required, validate.number, validate.phone]}/>
        <Field name="age" type="number" component={renderField} label="Age*"
               validate={[validate.required, validate.minValue18, validate.tooOld]}/>
      </div>
      <div className="ui section divider">
      </div>
    </FormSection>
  }
}

export default PassengerForm;