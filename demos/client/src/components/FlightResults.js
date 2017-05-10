import React from 'react';
import FlightCell from './FlightCell';
import travelOptions from '../data/travelOptions';

export default () => (
    <div>
        <h2>Flight Results</h2>

        <FlightCell changePriceSection={travelOptions.get('price').get(0)}
                    firstHour={travelOptions.get('hour').get(0)}
                    secondHour={travelOptions.get('hour').get(1)}  
                    firstCity={travelOptions.get('city').get(0)}    
                    secondCity={travelOptions.get('city').get(1)}/>

        <FlightCell changePriceSection={travelOptions.get('price').get(1)}
                    firstHour={travelOptions.get('hour').get(0)}
                    secondHour={travelOptions.get('hour').get(1)}  
                    firstCity={travelOptions.get('city').get(0)}    
                    secondCity={travelOptions.get('city').get(1)}/>

        <FlightCell changePriceSection={travelOptions.get('price').get(2)}
                    firstHour={travelOptions.get('hour').get(0)}
                    secondHour={travelOptions.get('hour').get(1)}  
                    firstCity={travelOptions.get('city').get(0)}    
                    secondCity={travelOptions.get('city').get(1)}/>
    </div>
)