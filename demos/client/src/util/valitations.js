const ERROR_REQUIRED = 'Required';
const ERROR_MIN_NAME_CHARS = 'Must be 15 characters or less';
const ERROR_NUM = 'Must be a valid number';
const ERROR_UNDER_AGE = 'Must be at least 18 years old';
const ERROR_EMAIL = 'Invalid email address';

// REGEX
const REGEX_EMAIL = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
const REGEX_PHONE = /^(0|[1-9][0-9]{9})$/i;

export const validate = values => {
  const errors = {};
  if (!values.name) {
    errors.name = ERROR_REQUIRED;
  } else if (values.name.length > 15) {
    errors.name = ERROR_MIN_NAME_CHARS;
  }
  if (!values.lastname) {
    errors.lastname = ERROR_REQUIRED;
  } else if (values.lastname.length > 15) {
    errors.lastname = ERROR_MIN_NAME_CHARS;
  }
  if (!values.email) {
    errors.email = ERROR_REQUIRED;
  } else if (!REGEX_EMAIL.test(values.email)) {
    errors.email = ERROR_EMAIL;
  }
  if (!values.age) {
    errors.age = ERROR_REQUIRED;
  } else if (isNaN(Number(values.age))) {
    errors.age = ERROR_NUM;
  } else if (Number(values.age) < 18) {
    errors.age = ERROR_UNDER_AGE;
  }
  if (!values.phone) {
    errors.phone = ERROR_REQUIRED;
  } else if (!REGEX_PHONE.test(values.phone)) {
    errors.phone = ERROR_NUM;
  }

  return errors
};
