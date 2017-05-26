import React from 'react';
import {Card, Radio} from 'semantic-ui-react';
import ItineraryOptions from '../../data/ItineraryOptions';

const FiltersCard = ({changeFilterLimit, selectedLimit}) => {
	return(
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
					{ItineraryOptions.get('filters').get('limits').map((limit) => {
					return(
						<div key={limit}>
							<Radio
								label={limit}
								name='limitRadioGroup'
								value={limit}
								checked={selectedLimit===limit}
								onChange={() => changeFilterLimit(limit)}
							/>
						</div>
					)})
				}
				</Card.Description>
				</Card.Content>
		</Card>
	)
}

export default FiltersCard;