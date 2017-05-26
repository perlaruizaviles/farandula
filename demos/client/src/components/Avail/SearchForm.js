import React from "react";
import {Field, FormSection, reduxForm} from "redux-form";
import {renderDatePicker, renderSearch} from "../../util/formFields";


const SearchForm = props => {
  const {handleSubmit, submitting, properties, actions} = props;
  return (
    <form onSubmit={handleSubmit} className="ui error form">

      <FormSection name="destiny-1">
        <div className="equal width fields">
          <Field name="departingAirport" component={renderSearch} results={properties.airports}
                 onSearchChange={(e, query, quantum = properties.airportTo) => actions.searchAirport(query, quantum)}/>

          <Field name="arrivalAirport" component={renderSearch} results={properties.airports}
                 onSearchChange={(e, query, quantum = properties.airportFrom) => actions.searchAirport(query, quantum)}/>

          <Field name="departingDate" type="text" component={renderDatePicker} label="Date*"/>

        </div>
      </FormSection>

      <FormSection name="destiny-2">
        <div className="equal width fields">
          <Field name="departingAirport" component={renderSearch} results={properties.airports}
                 onSearchChange={(e, query, quantum = properties.airportTo) => actions.searchAirport(query, quantum)}/>

          <Field name="arrivalAirport" component={renderSearch} results={properties.airports}
                 onSearchChange={(e, query, quantum = properties.airportFrom) => actions.searchAirport(query, quantum)}/>

          <Field name="departingDate" type="text" component={renderDatePicker} label="Date*"/>

        </div>
      </FormSection>

      <FormSection name="destiny-3">
        <div className="equal width fields">
          <Field name="departingAirport" component={renderSearch} results={properties.airports}
                 onSearchChange={(e, query, quantum = properties.airportTo) => actions.searchAirport(query, quantum)}/>

          <Field name="arrivalAirport" component={renderSearch} results={properties.airports}
                 onSearchChange={(e, query, quantum = properties.airportFrom) => actions.searchAirport(query, quantum)}/>

          <Field name="departingDate" type="text" component={renderDatePicker} label="Date*"/>

        </div>
      </FormSection>

      <div>
        <button className="ui orange button" type="submit" disabled={submitting}>Search</button>
      </div>
    </form>
  );
};

export default reduxForm({
  form: 'SearchForm', // a unique identifier for this form
})(SearchForm)
