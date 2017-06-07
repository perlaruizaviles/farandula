export const airlineNameByAirlegs = airlegs => {

  let airline;

  airlegs.map((airleg) => {
    return (
      airleg.segments.map((segment) => {
        if (!airline) {
          airline = segment.airLineMarketingName;
        } else {
          if (airline !== segment.airLineMarketingName) {
            airline = "multiple airlines"
          }
        }
        return airline;
      })
    );
  })
  return airline;
}