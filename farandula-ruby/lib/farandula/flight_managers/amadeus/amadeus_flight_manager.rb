require 'rest-client'
require_relative '../flight_manager.rb'
require 'farandula/models/itinerary'
require 'farandula/models/air_leg'
require 'farandula/models/segment'

module Farandula
  module FlightManagers
    module Amadeus

      class AmadeusFlightManager < FlightManager

        attr_accessor :api_key

        def initialize(api_key)
          @api_key = api_key
        end

        def get_avail(search_form)

          request = Amadeus::Request.new

          url_request = request.build_target_url_from_search!(search_form, api_key)

          puts url_request

          response = RestClient.get url_request

          build_itineries(response)

        end

        def build_itineries (response)

          itineries_list = []

          parsed = JSON.parse(response)

          parsed["results"].each {|result|

            result['itineraries'].each {|itinerary|

              itineray_result = Farandula::Itinerary.new

              itineries_list << build_air_legs(itineray_result, itinerary)

            }
          }

          itineries_list

        end

        private
        def build_air_legs (itinerary_object, itinerary_json_result)

          itinerary_object.departure_air_legs = build_leg(itinerary_json_result['outbound']['flights'])

          if itinerary_json_result['inbound']
            itinerary_object.returning_air_legs = build_leg(itinerary_json_result['inbound']['flights'])
          end

          itinerary_object

        end

        def build_leg (jsonObject)

          segments_array = jsonObject.map {|segment| build_segment(segment)}

          leg = AirLeg.new
          leg.id = "tempID";
          leg.departure_airport_code = segments_array[0].departure_airport_code
          leg.departure_date = segments_array[0].departure_date
          leg.arrival_airport_code = segments_array[segments_array.size - 1].arrival_airport_code
          leg.arrival_date = segments_array[segments_array.size - 1].arrival_date
          leg.segments = segments_array
          return leg;

        end

        def build_segment(segmentJson)

          departure_airport_data = segmentJson['origin']

          arrival_airport_data = segmentJson['destination']

          booking_info_data = segmentJson['booking_info']

          pricing_info_data = segmentJson['fare']

          segment = Farandula::Segment.new

          segment.operating_airline_code = segmentJson['operating_airline']
          #todo mapping of airlines code
          #segment.operating_airline_name =  airlinesCodeMap.get( segment.operating_airline_code )
          segment.operating_flight_number = segmentJson['flight_number']

          segment.marketing_airline_code = segmentJson['marketing_airline']
          #todo mapping of airlines code
          #segment.marketing_airline_name =  airlinesCodeMap.get( segment.marketing_airline_code )
          segment.marketing_flight_number = segmentJson['flight_number']

          segment.airplane_data = segmentJson['aircraft']

          #todo cabin class stuff as seat class

          #departure stuff
          segment.departure_airport_code = departure_airport_data['airport']
          segment.departure_terminal = departure_airport_data['terminal']
          #todo format time
          departure_date_time = segmentJson['departs_at']
          segment.departure_date = departure_date_time

          #arrival stuff
          segment.arrival_airport_code = arrival_airport_data['airport']
          segment.arrival_terminal = arrival_airport_data['terminal']
          #todo format time
          arrival_date_time = segmentJson['arrives_at']
          segment.arrival_date = arrival_date_time


          #todo duration flight

          puts segment.to_s

          segment

        end

      end #ends amadeusflightmanager
    end #ends Amadeus
  end #ends FlightManagers
end # ends Farandula