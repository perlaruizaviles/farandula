import { TravelPortFlightConnector } from '../connectors/travelport/travelportFlightConnector';
import moment = require('moment')
  var response:string = `<SOAP:Envelope xmlns:SOAP="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP:Body>
        <air:LowFareSearchRsp TraceId="trace" TransactionId="1E1D4E200A07643BE8D907576659040F" ResponseTime="4203" DistanceUnits="MI" CurrencyType="USD" xmlns:air="http://www.travelport.com/schema/air_v34_0" xmlns:common_v34_0="http://www.travelport.com/schema/common_v34_0">
            <common_v34_0:ResponseMessage Code="4039" Type="Warning" ProviderCode="1G">"Result size exceeded the maximum allowable and some results were discarded. It may be necessary to narrow your search using search modifiers."</common_v34_0:ResponseMessage>
            <air:FlightDetailsList>
                <air:FlightDetails Key="ZGf7Y07Q2BKArbdgAAAAAA==" Origin="DFW" Destination="MEX" DepartureTime="2017-07-28T19:40:00.000-05:00" ArrivalTime="2017-07-28T22:30:00.000-05:00" FlightTime="170" TravelTime="920" Equipment="EMJ" OriginTerminal="D" DestinationTerminal="2"/>
                <air:FlightDetails Key="ZGf7Y07Q2BKAtbdgAAAAAA==" Origin="MEX" Destination="CDG" DepartureTime="2017-07-28T23:55:00.000-05:00" ArrivalTime="2017-07-29T18:00:00.000+02:00" FlightTime="665" TravelTime="920" Equipment="789" OriginTerminal="2" DestinationTerminal="2E"/>
                <air:FlightDetails Key="ZGf7Y07Q2BKA+bdgAAAAAA==" Origin="DFW" Destination="MEX" DepartureTime="2017-07-28T12:40:00.000-05:00" ArrivalTime="2017-07-28T15:30:00.000-05:00" FlightTime="170" TravelTime="1340" Equipment="EMJ" OriginTerminal="D" DestinationTerminal="2"/>
            </air:FlightDetailsList>
            <air:AirSegmentList>
                <air:AirSegment Key="ZGf7Y07Q2BKAqbdgAAAAAA==" Group="0" Carrier="AM" FlightNumber="2683" Origin="DFW" Destination="MEX" DepartureTime="2017-07-28T19:40:00.000-05:00" ArrivalTime="2017-07-28T22:30:00.000-05:00" FlightTime="170" Distance="944" ETicketability="Yes" Equipment="EMJ" ChangeOfPlane="false" ParticipantLevel="Secure Sell" LinkAvailability="true" PolledAvailabilityOption="Polled avail used" OptionalServicesIndicator="false" AvailabilitySource="S" AvailabilityDisplayType="Fare Shop/Optimal Shop">
                    <air:CodeshareInfo>AEROLITORAL DBA AEROMEXICO CONNECT</air:CodeshareInfo>
                    <air:AirAvailInfo ProviderCode="1G"/>
                    <air:FlightDetailsRef Key="ZGf7Y07Q2BKArbdgAAAAAA=="/>
                </air:AirSegment>
                <air:AirSegment Key="ZGf7Y07Q2BKAsbdgAAAAAA==" Group="0" Carrier="AM" FlightNumber="3" Origin="MEX" Destination="CDG" DepartureTime="2017-07-28T23:55:00.000-05:00" ArrivalTime="2017-07-29T18:00:00.000+02:00" FlightTime="665" Distance="5735" ETicketability="Yes" Equipment="789" ChangeOfPlane="false" ParticipantLevel="Secure Sell" LinkAvailability="true" PolledAvailabilityOption="Polled avail used" OptionalServicesIndicator="false" AvailabilitySource="S" AvailabilityDisplayType="Fare Shop/Optimal Shop">
                    <air:AirAvailInfo ProviderCode="1G"/>
                    <air:FlightDetailsRef Key="ZGf7Y07Q2BKAtbdgAAAAAA=="/>
                </air:AirSegment>
                <air:AirSegment Key="ZGf7Y07Q2BKA9bdgAAAAAA==" Group="0" Carrier="AM" FlightNumber="2689" Origin="DFW" Destination="MEX" DepartureTime="2017-07-28T12:40:00.000-05:00" ArrivalTime="2017-07-28T15:30:00.000-05:00" FlightTime="170" Distance="944" ETicketability="Yes" Equipment="EMJ" ChangeOfPlane="false" ParticipantLevel="Secure Sell" LinkAvailability="true" PolledAvailabilityOption="Polled avail used" OptionalServicesIndicator="false" AvailabilitySource="S" AvailabilityDisplayType="Fare Shop/Optimal Shop">
                    <air:CodeshareInfo>AEROLITORAL DBA AEROMEXICO CONNECT</air:CodeshareInfo>
                    <air:AirAvailInfo ProviderCode="1G"/>
                    <air:FlightDetailsRef Key="ZGf7Y07Q2BKA+bdgAAAAAA=="/>
                </air:AirSegment>
            </air:AirSegmentList>
            <air:FareInfoList>
                <air:FareInfo Key="ZGf7Y07Q2BKAHcdgAAAAAA==" FareBasis="IONNL0PM" PassengerTypeCode="CNN" Origin="DFW" Destination="MEX" EffectiveDate="2017-07-07T18:33:00.000+01:00" DepartureDate="2017-07-28" Amount="USD412.00" NegotiatedFare="false">
                    <air:FareTicketDesignator Value="CH00"/>
                    <air:BaggageAllowance>
                        <air:NumberOfPieces>2</air:NumberOfPieces>
                        <air:MaxWeight/>
                    </air:BaggageAllowance>
                    <air:FareRuleKey FareInfoRef="ZGf7Y07Q2BKAHcdgAAAAAA==" ProviderCode="1G">6UUVoSldxwi03gXorv6OicbKj3F8T9EyxsqPcXxP0TIjSPOlaHfQe5cuasWd6i8Dly5qxZ3qLwOXLmrFneovA5cuasWd6i8Dly5qxZ3qLwOXLmrFneovAznioI+llHasM3ExqSoG051kDt6POLdTVyDWKw2reO+zQu0ZscBMSQ5KgtYRPan224blHFZlO2TdgsYdTzZyP3lJhmJkgBLGQcCIbRqMTJch6v9tEaRJgF5aeijkgxpswqJH7JsYjGL5fr06N/13ezRW8vSBNa8ZUmwC02UUzMsn5zl7QbIRajRkhORl5Y0+RNMs46uo4gLhir5wkVQHOuKXLmrFneovA5cuasWd6i8Dly5qxZ3qLwOXLmrFneovA5cuasWd6i8DnnUuWXdZ/ivzOoqKdr8JoSJ/HJ9mjemgzpsdKj28sen1OLjcwWy/+JJyDRnazR2YnXOpRJLy/Bxbuaf711T9cuVuG0bsK6zw</air:FareRuleKey>
                </air:FareInfo>
                <air:FareInfo Key="ZGf7Y07Q2BKANcdgAAAAAA==" FareBasis="YOAP" PassengerTypeCode="CNN" Origin="MEX" Destination="CDG" EffectiveDate="2017-07-07T18:33:00.000+01:00" DepartureDate="2017-07-28" Amount="USD1890.00" NegotiatedFare="false">
                    <air:FareTicketDesignator Value="CH15"/>
                    <air:FareSurcharge Key="ZGf7Y07Q2BKAOcdgAAAAAA==" Type="Other" Amount="NUC189.00"/>
                    <air:BaggageAllowance>
                        <air:NumberOfPieces>2</air:NumberOfPieces>
                        <air:MaxWeight/>
                    </air:BaggageAllowance>
                    <air:FareRuleKey FareInfoRef="ZGf7Y07Q2BKANcdgAAAAAA==" ProviderCode="1G">6UUVoSldxwi03gXorv6OicbKj3F8T9EyxsqPcXxP0TIjSPOlaHfQe5cuasWd6i8Dly5qxZ3qLwOXLmrFneovA5cuasWd6i8Dly5qxZ3qLwOXLmrFneovAznioI+llHasM3ExqSoG050YrFZ2txsEb1e8tTml6ur5GdZstTApXdDAuK7iuLTdMg7w/Jw4c0KHKGYJErQiD72F/oTXxxF6MeJYtF79PC3Y84PrlTL+yY3VW5TpURjX8TJhPzxD7Oc5AoaSrPFp62/qNbjwzJx7oo0sKBvhNXxacp4GLAoC0jJ582oBh9jJOhZQDCIIEeLrv4Xvb2u1Qx+/he9va7VDH7+F729rtUMfv4Xvb2u1Qx+/he9va7VDHzQapDbCAMr/In8cn2aN6aCXLmrFneovA3Tm1K4WiuAkCcnT3OM2Pe8HmCwZV/gVHY7ZBDXCHw0MoJJ7EVXf5HLhl+CoPNM+1A==</air:FareRuleKey>
                </air:FareInfo>
                <air:FareInfo Key="ZGf7Y07Q2BKA0bdgAAAAAA==" FareBasis="IONNL0PM" PassengerTypeCode="ADT" Origin="DFW" Destination="MEX" EffectiveDate="2017-07-07T18:33:00.000+01:00" DepartureDate="2017-07-28" Amount="USD412.00" NegotiatedFare="false">
                    <air:BaggageAllowance>
                        <air:NumberOfPieces>2</air:NumberOfPieces>
                        <air:MaxWeight/>
                    </air:BaggageAllowance>
                    <air:FareRuleKey FareInfoRef="ZGf7Y07Q2BKA0bdgAAAAAA==" ProviderCode="1G">6UUVoSldxwi03gXorv6OicbKj3F8T9EyxsqPcXxP0TIjSPOlaHfQe5cuasWd6i8Dly5qxZ3qLwOXLmrFneovA5cuasWd6i8Dly5qxZ3qLwOXLmrFneovAznioI+llHasM3ExqSoG051kDt6POLdTVyDWKw2reO+zQu0ZscBMSQ5KgtYRPan224blHFZlO2TdgsYdTzZyP3lJhmJkgBLGQcCIbRqMTJch6v9tEaRJgF5C/YIEuJEelqJH7JsYjGL5fr06N/13ezRW8vSBNa8ZUmwC02UUzMsn5zl7QbIRajRkhORl5Y0+RM85JzK1owZHir5wkVQHOuKXLmrFneovA5cuasWd6i8Dly5qxZ3qLwOXLmrFneovA5cuasWd6i8DnnUuWXdZ/ivzOoqKdr8JoSJ/HJ9mjemgzpsdKj28selkKfoRXTlgI5JyDRnazR2YnXOpRJLy/Bxbuaf711T9cuVuG0bsK6zw</air:FareRuleKey>
                </air:FareInfo>
                <air:FareInfo Key="ZGf7Y07Q2BKA6bdgAAAAAA==" FareBasis="YOAP" PassengerTypeCode="ADT" Origin="MEX" Destination="CDG" EffectiveDate="2017-07-07T18:33:00.000+01:00" DepartureDate="2017-07-28" Amount="USD2190.00" NegotiatedFare="false">
                    <air:FareSurcharge Key="ZGf7Y07Q2BKA7bdgAAAAAA==" Type="Other" Amount="NUC189.00"/>
                    <air:BaggageAllowance>
                        <air:NumberOfPieces>2</air:NumberOfPieces>
                        <air:MaxWeight/>
                    </air:BaggageAllowance>
                    <air:FareRuleKey FareInfoRef="ZGf7Y07Q2BKA6bdgAAAAAA==" ProviderCode="1G">6UUVoSldxwi03gXorv6OicbKj3F8T9EyxsqPcXxP0TIjSPOlaHfQe5cuasWd6i8Dly5qxZ3qLwOXLmrFneovA5cuasWd6i8Dly5qxZ3qLwOXLmrFneovAznioI+llHasM3ExqSoG050YrFZ2txsEb1wRDlyV2e62GqxwKzgb8d/AuK7iuLTdMg7w/Jw4c0KHKGYJErQiD72F/oTXxxF6MeJYtF79PC3YfoLT9JoAKrPVW5TpURjX8TJhPzxD7Oc5AoaSrPFp62/qNbjwzJx7oo0sKBvhNXxacp4GLAoC0jLH3MMcrxt1ps869eStP5yQv4Xvb2u1Qx+/he9va7VDH7+F729rtUMfv4Xvb2u1Qx+/he9va7VDHzQapDbCAMr/In8cn2aN6aCXLmrFneovA3Tm1K4WiuAkCcnT3OM2Pe8/uCk6ZI+Bho7ZBDXCHw0MoJJ7EVXf5HLhl+CoPNM+1A==</air:FareRuleKey>
                </air:FareInfo>
            </air:FareInfoList>
            <air:RouteList>
                <air:Route Key="ZGf7Y07Q2BKAUcdgAAAAAA==">
                    <air:Leg Key="ZGf7Y07Q2BKAubdgAAAAAA==" Group="0" Origin="DFW" Destination="PAR"/>
                </air:Route>
            </air:RouteList>
            <air:AirPricingSolution Key="ZGf7Y07Q2BKApbdgAAAAAA==" TotalPrice="USD12554.50" BasePrice="USD12410.00" ApproximateTotalPrice="USD12554.50" ApproximateBasePrice="USD12410.00" Taxes="USD144.50" ApproximateTaxes="USD144.50">
                <air:Journey TravelTime="P0DT15H20M0S">
                    <air:AirSegmentRef Key="ZGf7Y07Q2BKAqbdgAAAAAA=="/>
                    <air:AirSegmentRef Key="ZGf7Y07Q2BKAsbdgAAAAAA=="/>
                </air:Journey>
                <air:LegRef Key="ZGf7Y07Q2BKAubdgAAAAAA=="/>
                <air:AirPricingInfo Key="ZGf7Y07Q2BKAvbdgAAAAAA==" TotalPrice="USD2630.90" BasePrice="USD2602.00" ApproximateTotalPrice="USD2630.90" ApproximateBasePrice="USD2602.00" Taxes="USD28.90" ApproximateTaxes="USD28.90" LatestTicketingTime="2017-07-07" PricingMethod="Guaranteed" ETicketability="Yes" PlatingCarrier="AM" ProviderCode="1G">
                    <air:FareInfoRef Key="ZGf7Y07Q2BKA0bdgAAAAAA=="/>
                    <air:FareInfoRef Key="ZGf7Y07Q2BKA6bdgAAAAAA=="/>
                    <air:BookingInfo BookingCode="Y" BookingCount="7" CabinClass="Economy" FareInfoRef="ZGf7Y07Q2BKA0bdgAAAAAA==" SegmentRef="ZGf7Y07Q2BKAqbdgAAAAAA=="/>
                    <air:BookingInfo BookingCode="Y" BookingCount="7" CabinClass="Economy" FareInfoRef="ZGf7Y07Q2BKA6bdgAAAAAA==" SegmentRef="ZGf7Y07Q2BKAsbdgAAAAAA=="/>
                    <air:TaxInfo Category="AY" Amount="USD5.60"/>
                    <air:TaxInfo Category="US" Amount="USD18.00"/>
                    <air:TaxInfo Category="XF" Amount="USD4.50">
                        <common_v34_0:TaxDetail Amount="USD4.50" OriginAirport="DFW"/>
                    </air:TaxInfo>
                    <air:TaxInfo Category="YR" Amount="USD0.80"/>
                    <air:FareCalc>DFW AM MEX 412.00IONNL0PM AM PAR Q189.00 2001.00YOAP NUC2602.00END ROE1.0</air:FareCalc>
                    <air:PassengerType Code="ADT" Age="0"/>
                    <air:PassengerType Code="ADT" Age="0"/>
                    <air:PassengerType Code="ADT" Age="0"/>
                </air:AirPricingInfo>
                <air:AirPricingInfo Key="ZGf7Y07Q2BKACcdgAAAAAA==" TotalPrice="USD2330.90" BasePrice="USD2302.00" ApproximateTotalPrice="USD2330.90" ApproximateBasePrice="USD2302.00" Taxes="USD28.90" ApproximateTaxes="USD28.90" LatestTicketingTime="2017-07-07" PricingMethod="Guaranteed" ETicketability="Yes" PlatingCarrier="AM" ProviderCode="1G">
                    <air:FareInfoRef Key="ZGf7Y07Q2BKAHcdgAAAAAA=="/>
                    <air:FareInfoRef Key="ZGf7Y07Q2BKANcdgAAAAAA=="/>
                    <air:BookingInfo BookingCode="Y" BookingCount="7" CabinClass="Economy" FareInfoRef="ZGf7Y07Q2BKAHcdgAAAAAA==" SegmentRef="ZGf7Y07Q2BKAqbdgAAAAAA=="/>
                    <air:BookingInfo BookingCode="Y" BookingCount="7" CabinClass="Economy" FareInfoRef="ZGf7Y07Q2BKANcdgAAAAAA==" SegmentRef="ZGf7Y07Q2BKAsbdgAAAAAA=="/>
                    <air:TaxInfo Category="AY" Amount="USD5.60"/>
                    <air:TaxInfo Category="US" Amount="USD18.00"/>
                    <air:TaxInfo Category="XF" Amount="USD4.50">
                        <common_v34_0:TaxDetail Amount="USD4.50" OriginAirport="DFW"/>
                    </air:TaxInfo>
                    <air:TaxInfo Category="YR" Amount="USD0.80"/>
                    <air:FareCalc>DFW AM MEX 412.00IONNL0PM/CH00 AM PAR Q189.00 1700.85YOAP/CH15 NUC2301.85END ROE1.0</air:FareCalc>
                    <air:PassengerType Code="CNN"/>
                    <air:PassengerType Code="CNN"/>
                </air:AirPricingInfo>
                <air:Connection SegmentIndex="0"/>
            </air:AirPricingSolution>
            <air:AirPricingSolution Key="ZGf7Y07Q2BKATcdgAAAAAA==" TotalPrice="USD12554.50" BasePrice="USD12410.00" ApproximateTotalPrice="USD12554.50" ApproximateBasePrice="USD12410.00" Taxes="USD144.50" ApproximateTaxes="USD144.50">
                <air:Journey TravelTime="P0DT22H20M0S">
                    <air:AirSegmentRef Key="ZGf7Y07Q2BKA9bdgAAAAAA=="/>
                    <air:AirSegmentRef Key="ZGf7Y07Q2BKAsbdgAAAAAA=="/>
                </air:Journey>
                <air:LegRef Key="ZGf7Y07Q2BKAubdgAAAAAA=="/>
                <air:AirPricingInfo Key="ZGf7Y07Q2BKA1bdgAAAAAA==" TotalPrice="USD2630.90" BasePrice="USD2602.00" ApproximateTotalPrice="USD2630.90" ApproximateBasePrice="USD2602.00" Taxes="USD28.90" ApproximateTaxes="USD28.90" LatestTicketingTime="2017-07-07" PricingMethod="Guaranteed" ETicketability="Yes" PlatingCarrier="AM" ProviderCode="1G">
                    <air:FareInfoRef Key="ZGf7Y07Q2BKA0bdgAAAAAA=="/>
                    <air:FareInfoRef Key="ZGf7Y07Q2BKA6bdgAAAAAA=="/>
                    <air:BookingInfo BookingCode="Y" BookingCount="7" CabinClass="Economy" FareInfoRef="ZGf7Y07Q2BKA0bdgAAAAAA==" SegmentRef="ZGf7Y07Q2BKA9bdgAAAAAA=="/>
                    <air:BookingInfo BookingCode="Y" BookingCount="7" CabinClass="Economy" FareInfoRef="ZGf7Y07Q2BKA6bdgAAAAAA==" SegmentRef="ZGf7Y07Q2BKAsbdgAAAAAA=="/>
                    <air:TaxInfo Category="AY" Amount="USD5.60"/>
                    <air:TaxInfo Category="US" Amount="USD18.00"/>
                    <air:TaxInfo Category="XF" Amount="USD4.50">
                        <common_v34_0:TaxDetail Amount="USD4.50" OriginAirport="DFW"/>
                    </air:TaxInfo>
                    <air:TaxInfo Category="YR" Amount="USD0.80"/>
                    <air:FareCalc>DFW AM MEX 412.00IONNL0PM AM PAR Q189.00 2001.00YOAP NUC2602.00END ROE1.0</air:FareCalc>
                    <air:PassengerType Code="ADT" Age="0"/>
                    <air:PassengerType Code="ADT" Age="0"/>
                    <air:PassengerType Code="ADT" Age="0"/>
                </air:AirPricingInfo>
                <air:AirPricingInfo Key="ZGf7Y07Q2BKAIcdgAAAAAA==" TotalPrice="USD2330.90" BasePrice="USD2302.00" ApproximateTotalPrice="USD2330.90" ApproximateBasePrice="USD2302.00" Taxes="USD28.90" ApproximateTaxes="USD28.90" LatestTicketingTime="2017-07-07" PricingMethod="Guaranteed" ETicketability="Yes" PlatingCarrier="AM" ProviderCode="1G">
                    <air:FareInfoRef Key="ZGf7Y07Q2BKAHcdgAAAAAA=="/>
                    <air:FareInfoRef Key="ZGf7Y07Q2BKANcdgAAAAAA=="/>
                    <air:BookingInfo BookingCode="Y" BookingCount="7" CabinClass="Economy" FareInfoRef="ZGf7Y07Q2BKAHcdgAAAAAA==" SegmentRef="ZGf7Y07Q2BKA9bdgAAAAAA=="/>
                    <air:BookingInfo BookingCode="Y" BookingCount="7" CabinClass="Economy" FareInfoRef="ZGf7Y07Q2BKANcdgAAAAAA==" SegmentRef="ZGf7Y07Q2BKAsbdgAAAAAA=="/>
                    <air:TaxInfo Category="AY" Amount="USD5.60"/>
                    <air:TaxInfo Category="US" Amount="USD18.00"/>
                    <air:TaxInfo Category="XF" Amount="USD4.50">
                        <common_v34_0:TaxDetail Amount="USD4.50" OriginAirport="DFW"/>
                    </air:TaxInfo>
                    <air:TaxInfo Category="YR" Amount="USD0.80"/>
                    <air:FareCalc>DFW AM MEX 412.00IONNL0PM/CH00 AM PAR Q189.00 1700.85YOAP/CH15 NUC2301.85END ROE1.0</air:FareCalc>
                    <air:PassengerType Code="CNN"/>
                    <air:PassengerType Code="CNN"/>
                </air:AirPricingInfo>
                <air:Connection SegmentIndex="0"/>
            </air:AirPricingSolution>
        </air:LowFareSearchRsp>
    </SOAP:Body>
</SOAP:Envelope>`
  
  test('Parse XML to OBJ', () => {
    let tvpc = new TravelPortFlightConnector('oneWay')
    tvpc.parseResponse(response, (res:any) => expect(res).toBeTruthy())
  })