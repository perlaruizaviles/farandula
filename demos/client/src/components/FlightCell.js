import React from 'react'
import { Segment, Grid } from 'semantic-ui-react'
import PriceSection from './PriceSection'
import FlightInfo from './FlightSection'
import DetailSection from './DetailSection'

const FlightCell = ({changePriceSection, firstHour, secondHour, firstCity, secondCity}) => {
    return(
            <Segment className="raised">
                <Grid divided={true}>
                    <Grid.Row columns={3}>
                        <Grid.Column>
                            <PriceSection changePriceSection={changePriceSection}/>
                        </Grid.Column>
                        <Grid.Column>
                            <FlightInfo firstHour={firstHour} secondHour={secondHour} firstCity={firstCity} secondCity={secondCity}/>
                        </Grid.Column>
                        <Grid.Column style={{textAlign: 'center'}}>
                            <DetailSection/>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
            </Segment>
    )
}

export default FlightCell;
