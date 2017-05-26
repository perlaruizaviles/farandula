import {singularize, titleize} from "inflection";

export const countTravelers = (config) => {
  let count = config
    .get('travelers')
    .reduce((r, k) => r + k);
  if (count < 0)
    throw new Error("Malformed flight settings");
  return count;
};

export const configTravelString = (config) => {
  let count = countTravelers(config);
  let cabin = config.get('cabin');

  if (count < 1) throw new Error('malformed flightSettings: passenger count < 1');
  if (count === 1) {
    let travelerType = config.get('travelers')
      .filter(count => count !== 0)
      .keySeq().get(0);
    return count + " " + titleize(singularize(travelerType)) + ", " + titleize(cabin);
  } else {
    return count + " Travelers, " + titleize(cabin);
  }
};