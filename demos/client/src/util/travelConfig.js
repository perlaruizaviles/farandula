const countTravelers = (config) => {
    let count = config
        .get('travelers')
        .reduce((r, k) => r + k);
    if (count < 0) 
        throw new Error("Malformed flight settings");
    return count;
}

export {countTravelers};