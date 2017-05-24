import React from "react";

const renderFieldWithoutError = (input, label, type) => (
  <div className="field">
    <label>{label}</label>
    <div className="ui input">
      <input {...input} placeholder={label} type={type}/>
    </div>
  </div>
);

const renderFieldWithError = (input, label, type, touched, error) => (
  <div className="error field">
    <label>{label} - <i aria-hidden="true" className="red warning sign icon"/>{error}</label>
    <div className="ui input">
      <input {...input} placeholder={label} type={type}/>
    </div>
  </div>
);

const renderSelectFieldWithoutError = ( input, label, type, children ) => (
  <div className="field">
    <label>{label}</label>
    <div>
      <select {...input}>
        {children}
      </select>
    </div>
  </div>
);

const renderSelectFieldWithError = ( input, label, type,touched,error, children ) => (
  <div className="error field">
    <label>{label} - <i aria-hidden="true" className="red warning sign icon"/>{error}</label>
    <div>
      <select {...input}>
        {children}
      </select>
    </div>
  </div>
);

export const renderField = ({input, label, type, meta: {touched, error}, children}) => {

  if(type === "select"){
    if (touched && (error)) {
      return renderSelectFieldWithError(input, label, type, touched, error, children);
    }
    return renderSelectFieldWithoutError(input, label, type, children);
  }else if (touched && (error)) {
    return renderFieldWithError(input, label, type, touched, error);
  }
  return renderFieldWithoutError(input, label, type);
};
