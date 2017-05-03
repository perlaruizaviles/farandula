export const changeTravelType = type => {
  return {
    type: 'CHANGE_TRAVEL_TYPE',
    value: type
  };
};

export const changeTravelDate = date => {
  return {
    type: 'CHANGE_TRAVEL_DATE',
    value: date
  }
}
