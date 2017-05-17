import React from 'react';
import FlightCell from './FlightCell';
import travelOptions from '../data/travelOptions';

const FlightList = ({availableFlights}) => (
    <div>
        <button onClick={()=>console.log(availableFlights.get(0).key)}>click me!</button>
        {
            availableFlights.map((itinerary)=>{
                return (
                    <FlightCell key={itinerary.key} changePriceSection={itinerary.fares.total.amount.toString()}
                        firstHour={travelOptions.get('hour').get(0)}
                        secondHour={travelOptions.get('hour').get(1)}  
                        firstCity={itinerary.departureAirleg.departureAirport.iata}    
                        secondCity={travelOptions.get('city').get(1)}/>
                )
            })
        }
    </div>
);

export default FlightList;