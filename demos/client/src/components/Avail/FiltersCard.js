import React from 'react';
import {Card, Radio, Checkbox} from 'semantic-ui-react';
import ItineraryOptions from '../../data/ItineraryOptions';
import {titleize} from "inflection";

const FiltersCard = ({changeFilterLimit, selectedLimit, airlines, changeAirlinesFilter}) => {

		let limits = ItineraryOptions
				.get('filters')
				.get('limits');

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
												return (
														<div key={airline}>
																<Checkbox label={titleize(airline)} onChange={(x, y) => changeAirlinesFilter(airline, y.checked)}/>
														</div>
												)
										})}
								</Card.Description>
						</Card.Content>
				</Card>
		)
}

export default FiltersCard;