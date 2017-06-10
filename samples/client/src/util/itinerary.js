export const airlineNameByAirlegs = airlegs => {

  let airline;

  airlegs.map((airleg) => {
    return (
      airleg.segments.map((segment) => {
        if (!airline) {
          airline = segment.airLineMarketingName;
        } else {
          if (airline !== segment.airLineMarketingName) {
            airline = "Multiple Airlines"
          }
        }
        return airline;
      })
    );
  })
  return airline;
}