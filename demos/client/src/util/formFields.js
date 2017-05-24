import React from "react";

const renderFieldWithError = (input, label, type, touched, error) => (
  <div className={(error && touched)? "error field": "field"}>
    <label>{label} {(error && touched)? `- ${error}`:""}</label>
    <div className="ui input">
      <input {...input} placeholder={label} type={type}/>
    </div>
  </div>
);


const renderSelectFieldWithError = ( input, label, type, touched, error, children ) => (
  <div className={(error && touched)? "error field": "field"}>
    <label>{label} {(error && touched)? `- ${error}`:""}</label>
    <div className="ui input">
      <select {...input}>
        {children}
      </select>
    </div>
  </div>
);

export const renderField = ({input, label, type, meta: {touched, error}, children}) => {
  if(type === "select"){
    return renderSelectFieldWithError(input, label, type, touched, error, children);
  }
  return renderFieldWithError(input, label, type, touched, error);
};
