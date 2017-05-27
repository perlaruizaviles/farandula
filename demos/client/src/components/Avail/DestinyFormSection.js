import React from "react";
import {Field, FormSection} from "redux-form";
import {renderDatePicker, renderSearch} from "../../util/formFields";
import {required} from "../../util/valitations";

const DestinyFormSection = ({name, properties, actions}) => {
    return(
      <FormSection name={name}>
        <div className="equal width fields">
          <Field name="departingAirport"
                 component={renderSearch}
                 results={properties.airports}
                 validate={required}
                 onSearchChange={(e, query, quantum = properties.airportTo) => actions.searchAirport(query, quantum)}/>

          <Field name="arrivalAirport"
                 component={renderSearch}
                 results={properties.airports}
                 validate={required}
                 onSearchChange={(e, query, quantum = properties.airportFrom) => actions.searchAirport(query, quantum)}/>

          <Field name="departingDate"
                 component={renderDatePicker}
                 minDate={properties.minDate}
                 maxDate={properties.maxDate}
                 validate={required}/>

        </div>
      </FormSection>
    );
};

export default DestinyFormSection;