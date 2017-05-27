import React from "react";
import {reduxForm} from "redux-form";
import DestinyFormSection from "./DestinyFormSection";


const SearchForm = props => {
  const {handleSubmit, submitting, properties, actions} = props;
  return (
    <form onSubmit={handleSubmit} className="ui error form">

      <DestinyFormSection name="1" properties={properties} actions={actions}/>

      <div>
        <button className="ui orange button" type="submit" disabled={submitting}>Search</button>
      </div>
    </form>
  );
};

export default reduxForm({
  form: 'SearchForm', // a unique identifier for this form
})(SearchForm)
