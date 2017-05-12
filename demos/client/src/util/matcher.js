export const getIata = (airport) =>{
  return airport.slice(airport.indexOf(" - ")+3).trim();
};