import React from "react";
import {reduxForm} from "redux-form";
import DestinyFormSection from "./DestinyFormSection";

const SearchForm = props => {
  const {handleSubmit, submitting, properties, actions} = props;
  return (
    <div>
      <form onSubmit={handleSubmit} className="ui error form">
        {
          properties.destinies.map(destiny => <DestinyFormSection key={destiny} name={"destiny-" + destiny}
                                                                  properties={properties}
                                                                  actions={actions}/>)
        }
        <div>
          <button className="ui orange button" type="submit" disabled={submitting}>Search</button>
        </div>
      </form>
    </div>
  );
};

export default reduxForm({
  form: 'SearchForm', // a unique identifier for this form
})(SearchForm)
