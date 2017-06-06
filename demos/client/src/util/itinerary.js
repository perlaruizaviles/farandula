export const airlineNameByAirlegs = airlegs => {
	let airline;
	let result;

	airlegs.map((airleg) => {
		return(
			airleg.segments.map((segment) => {
				if(!airline){
					airline = segment.airLineMarketingName
				}else{
					if(airline!==segment.airLineMarketingName) {
						result = "multi"
					}
				}
				return result;
			})
		);
	})
	
	return result;
}