import React from 'react';
import {Card, Radio, Checkbox} from 'semantic-ui-react';
import ItineraryOptions from '../../data/ItineraryOptions';

const FiltersCard = ({changeFilterLimit, selectedLimit, changeAirlinesFilter}) => {

		let limits = ItineraryOptions
				.get('filters')
				.get('limits');
		let airlines = ItineraryOptions
				.get('filters')
				.get('airlines');

		return (
				<Card className='orange fixed'>
						<Card.Content>
								<Card.Header>
										Filters
								</Card.Header>
						</Card.Content>
						<Card.Content>
								<Card.Meta>
										Max itineraries:
								</Card.Meta>
								<Card.Description>
										{limits.map((limit) => {
												return (
														<div key={limit}>
																<Radio
																		label={limit}
																		name='limitRadioGroup'
																		value={limit}
																		checked={selectedLimit === limit}
																		onChange={() => changeFilterLimit(limit)}/>
														</div>
												)
										})}
								</Card.Description>
						</Card.Content>
						<Card.Content>
								<Card.Meta>
										Airlines:
								</Card.Meta>
								<Card.Description>
										{airlines.map((airline) => {
											let airlineName = airline.get('name');
												return (
														<div key={airlineName}>
																<Checkbox label={airlineName} onChange={() => changeAirlinesFilter(airlineName)}/>
														</div>
												)
										})}
								</Card.Description>
						</Card.Content>
				</Card>
		)
}

export default FiltersCard;