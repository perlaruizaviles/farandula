require_relative '../../lib/models/flights'
require_relative '../../lib/models/flight_itinerary'
require_relative '../../lib/models/itinerary_fares'

module FarandulaSample

  class ItineraryHelper

    def self.get_flight_itinerary_from_itinerary( itinerary_list, type  )

      itinerary_list.map do |itinerary|

        fares_from_itinerary = itinerary.fares
        itinerary_fares   = fares_from_itinerary.nil? ? ItineraryFares.new : parse_fares_to_itinerary_fares( fares_from_itinerary )
        flight_list       = get_flights_from_itinerary(itinerary)
        FarandulaSample::FlightItinerary.new(12345, type, flight_list, itinerary_fares )
      end

    end

    def self.parse_fares_to_itinerary_fares(fares)
      itinerary_fares             = ItineraryFares.new
      itinerary_fares.base_price  = fares.base_price
      itinerary_fares.taxes_price = fares.taxes_price
      itinerary_fares.total_price = fares.total_price
      itinerary_fares
    end

    def self.get_flights_from_itinerary(itinerary)
     itinerary.air_legs.map do | airleg |
       parse_airleg_to_flight( airleg )
     end
    end

    def self.parse_airleg_to_flight(airleg)

      departure_airport = airleg.departure_airport_code
      arrival_airport   = airleg.arrival_airport_code
      departure_date    = airleg.departure_date
      arrival_date      = airleg.arrival_date

      flight_segment_list = airleg.segments.select do | segment | !segment.nil? end

      Flight.new( departure_airport, departure_date, arrival_airport, arrival_date, flight_segment_list )

    end

  end

end
