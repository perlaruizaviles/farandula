const ERROR_REQUIRED = 'Required';
const ERROR_MIN_NAME_CHARS = 'Must be 15 characters or less';
const ERROR_NUM = 'Must be a valid number';
const ERROR_UNDER_AGE = 'Must be at least 18 years old';
const ERROR_EMAIL = 'Invalid email address';

// REGEX
const REGEX_EMAIL = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
const REGEX_PHONE = /^(0|[1-9][0-9]{9})$/i;

export const validate = values => {
  console.log(values);
  const errors = {};
  if (!values.passenger) {
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

export const required = value => (value ? undefined : 'Required');
export const maxLength = max => value =>
  value && value.length > max ? `Must be ${max} characters or less` : undefined;
export const maxLength15 = maxLength(15);
export const number = value =>
  value && isNaN(Number(value)) ? 'Must be a number' : undefined;
export const minValue = min => value =>
  value && value < min ? `Must be at least ${min}` : undefined;
export const minValue18 = minValue(18);
export const email = value =>
  value && !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value)
    ? 'Invalid email address'
    : undefined;
export const tooOld = value =>
  value && value > 65 ? 'You might be too old for this' : undefined;
export const aol = value =>
  value && /.+@aol\.com/.test(value)
    ? 'Really? You still use AOL for your email?'
    : undefined;