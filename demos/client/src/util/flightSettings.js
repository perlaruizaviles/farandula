const countPassengers = (settings) => {
  let count = settings.get('passengers').reduce((r, k) => r+k);
  if (count < 0)
    throw new Error("malformed flight settings");
  return count;
};


export {countPassengers};