export const airlineNameByAirlegs = airlegs => {
	let result;

	airlegs.map((airleg) => {
		let airline;
		return(
			airleg.segments.map((segment) => {
				if(!airline){
					airline = segment.airLineMarketingName;
					result = airline;
				}else{
					if(airline!==segment.airLineMarketingName) {
						result = "multiple airlines"
						return result;
					}
				}
				return result;
			})
		);
	})
	return result;
}