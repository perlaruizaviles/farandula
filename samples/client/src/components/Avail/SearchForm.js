import React from "react";
import {reduxForm} from "redux-form";
import DestinyFormSection from "./DestinyFormSection";
import {multiCityValidation} from "../../util/valitations";
import {Field} from "redux-form";
import {renderDatePicker} from "../../util/formFields";
import {required} from "../../util/valitations";


class SearchForm extends React.Component {
  componentWillReceiveProps(nextProps){
    if(this.props.properties.selectedType !== nextProps.properties.selectedType){
      this.props.reset();
    }
  }

  render(){

  const {handleSubmit, submitting, properties, destinies, actions} = this.props;

  function renderOnRoundTrip(type) {
    if (type === "roundTrip"){
      return <Field name="arrivalDate"
                    component={renderDatePicker}
                    minDate={properties.minDate}
                    maxDate={properties.maxDate}
                    validate={required}/>
    }
  }

  return (
    <div>
      <form onSubmit={handleSubmit} className="ui error form">
        {
          destinies.map(destiny => <DestinyFormSection key={destiny} name={"destiny-" + destiny}
                                                                  properties={properties}
                                                                  actions={actions}/>)
        }

        {renderOnRoundTrip(properties.selectedType)}

        <div>
          <button className="ui orange button" type="submit" disabled={submitting}>Search</button>
        </div>
      </form>
    </div>
  );
  }
}

export default reduxForm({
  form: 'SearchForm', // a unique identifier for this form
  validate: multiCityValidation,
})(SearchForm)
