// REGEX
const REGEX_EMAIL = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
const REGEX_PHONE = /^(0|[1-9][0-9]{9})$/i;
const REGEX_ZIP = /^\d{5}(?:[-\s]\d{4})?$/i;
const REGEX_CREDIT_CARD = /(5[1-5]\d{14})|(4\d{12}(\d{3})?)|(3[47]\d{13})|(6011\d{14})|((30[0-5]|36\d|38\d)\d{11})$/i;
const REGEX_SECURITY_CODE = /^[0-9]{3,4}$/i;
const REGEX_ALPHA_NUM = /[^a-zA-Z0-9 ]/i;


export const required = value => (value ? undefined : 'Required');

export const maxLength = max => value =>
  value && value.length > max ? `Must be ${max} characters or less` : undefined;

export const maxLength15 = maxLength(15);

export const minLength = min => value =>
  value && value.length < min ? `Must be ${min} characters or more` : undefined;

export const minLength5 = minLength(5);
export const minLength2 = minLength(2);

export const number = value =>
  value && isNaN(Number(value)) ? 'Must be a number' : undefined;

export const minValue = min => value =>
  value && value < min ? `Must be at least ${min}` : undefined;

export const minValue18 = minValue(18);

export const email = value =>
  value && !REGEX_EMAIL.test(value)
    ? 'Invalid email address'
    : undefined;

export const tooOld = value =>
  value && value > 105 ? 'You might be too old for this' : undefined;

export const aol = value =>
  value && /.+@aol\.com/.test(value)
    ? 'Really? You still use AOL for your email?'
    : undefined;

export const phone = value =>
  value && !REGEX_PHONE.test(value)
    ? 'Invalid phone number'
    : undefined;

export const zip = value =>
  value && !REGEX_ZIP.test(value)
    ? 'Invalid postal/zip code'
    : undefined;

export const creditCard = value =>
  value && !REGEX_CREDIT_CARD.test(value)
    ? 'Invalid credit card #'
    : undefined;

export const securityCode = value =>
  value && !REGEX_SECURITY_CODE.test(value)
    ? 'Invalid code'
    : undefined;

export const alphaNum = value =>
  value && REGEX_ALPHA_NUM.test(value)
    ? 'Only alpha-numeric characters'
    : undefined;


export const multiCityValidation = values => {
  const errors = {};
  for (let destiny in values) {
    if (values.hasOwnProperty(destiny) && destiny !== "arrivalDate") {
      if (JSON.stringify(values[destiny].departingAirport) === JSON.stringify(values[destiny].arrivalAirport)) {
        errors[destiny] = {};
        errors[destiny].departingAirport = "Date error";
        errors[destiny].arrivalAirport = "Date error";
      }
    }
  }

  let dates = [];
  let dateNames = [];
  for (let destiny in values) {
    if (values.hasOwnProperty(destiny) && values[destiny].departingDate !== undefined) {
      dates.push(values[destiny].departingDate);
      dateNames.push(destiny)
    }
  }

  if (dates.length > 1) {
    for (let i = 1; i < dates.length; i++) {
      if (dates[i].diff(dates[i - 1]) <= 0) {
        errors[dateNames[i]] = {};
        errors[dateNames[i]].departingDate = "Date error"; //TODO: Compare with all dates
      }
    }
  }else if (values.arrivalDate){
    if (values.arrivalDate.diff(dates[0]) <= 0) {
      errors.arrivalDate = {};
      errors.arrivalDate = "Arrival date error";
    }
  }

  return errors
};


// Valid Credit cards
// MasterCard: 5212345678901234
// Visa 1: 4123456789012
// Visa 2: 4123456789012345
// Amex: 371234567890123
// Discover: 601112345678901234
// Diners Club: 38812345678901