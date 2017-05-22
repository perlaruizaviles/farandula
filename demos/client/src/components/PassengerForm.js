import React from "react";
import {Field, FormSection} from "redux-form";
import {renderField} from "../util/formFields";
import * as validate from "../util/valitations";

class PassengerForm extends React.Component {
   render() {
    return <FormSection name={this.props.name}>
      <div className="equal width fields">
        <Field name="name" type="text" component={renderField} label="Name*" validate={[validate.required,validate.maxLength15]}/>
        <Field name="lastname" type="text" component={renderField} label="Lastname*" validate={[validate.required, validate.maxLength15]}/>
      </div>
      <div className="equal width fields">
        <Field name="middlename" type="text" component={renderField} label="Middle Name" validate={validate.maxLength15}/>
        <Field name="email" type="email" component={renderField} label="Email*" validate={[validate.required,validate.email, validate.aol]}/>
      </div>
      <div className="equal width fields">
        <div className="field">
          <label>Gender</label>
          <Field name="gender" component="select" validate={validate.required}>
            <option />
            <option value="female">Female</option>
            <option value="male">Male</option>
          </Field>
        </div>
        <Field name="phone" type="tel" component={renderField} label="Phone #*" validate={[validate.required,validate.number]}/>
        <Field name="age" type="number" component={renderField} label="Age*" validate={[validate.required,validate.minValue18]}/>
      </div>
      <div className="ui section divider">
      </div>
    </FormSection>
  }
}

export default PassengerForm;