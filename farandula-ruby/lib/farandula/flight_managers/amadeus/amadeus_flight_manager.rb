require 'rest-client'
require 'yaml'
require_relative '../flight_manager.rb'
require 'farandula/models/itinerary'
require 'farandula/models/air_leg'
require 'farandula/models/segment'
require 'farandula/models/seat'
require_relative '../../constants.rb'

module Farandula
  module FlightManagers
    module Amadeus

      class AmadeusFlightManager < FlightManager

        attr_accessor :api_key

        def initialize(api_key)
          @api_key = api_key

          @airline_code_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/' + "airlinesCode.yml" )

          #
          # file        = File.read(File.dirname(__FILE__) + '/../../assets/' + file_name)
          # parsed      = JSON.parse(file)
          # @hashCities = Hash.new
          # @hashCities = parsed["airports"].map { |airport| [airport['iata'].downcase, build_city(airport)] }.to_h
          #
        end

        def get_avail(search_form)

          request = Amadeus::Request.new

          url_request = request.build_target_url_from_search!(search_form, api_key)

          response = RestClient.get url_request

          build_itineries(response)

        end

        def build_itineries (response)

          itineries_list = []

          parsed = JSON.parse(response)

          itinerary_id = 0

          parsed["results"].each {|result|

            result['itineraries'].each {|itinerary|

              itineray_result = Farandula::Itinerary.new

              itinerary_id +=1

              itineries_list << build_air_legs(itineray_result, itinerary, itinerary_id)

            }
          }

          itineries_list

        end

        private
        def build_air_legs (itinerary_object, itinerary_json_result, id = nil)

          itinerary_object.departure_air_legs = build_leg(itinerary_json_result['outbound']['flights'], 1)

          if itinerary_json_result['inbound']
            itinerary_object.returning_air_legs = build_leg(itinerary_json_result['inbound']['flights'], 1)
          end

          itinerary_object.id = id
          itinerary_object

        end

        def build_leg (jsonObject, id = nil)

          segment_id = 0
          segments_array = jsonObject.map {|segment|
            segment_id =+ 1
            build_segment(segment, segment_id)
          }

          leg = AirLeg.new
          leg.id = id;
          leg.departure_airport_code = segments_array[0].departure_airport_code
          leg.departure_date = segments_array[0].departure_date
          leg.arrival_airport_code = segments_array[segments_array.size - 1].arrival_airport_code
          leg.arrival_date = segments_array[segments_array.size - 1].arrival_date
          leg.segments = segments_array
          return leg;

        end

        def build_segment(segmentJson, segment_key)

          departure_airport_data = segmentJson['origin']

          arrival_airport_data = segmentJson['destination']

          booking_info_data = segmentJson['booking_info']

          pricing_info_data = segmentJson['fare']

          segment = Farandula::Segment.new

          segment.key = segment_key

          segment.operating_airline_code = segmentJson['operating_airline']
          segment.operating_airline_name = @airline_code_map[segment.operating_airline_code]
          segment.operating_flight_number = segmentJson['flight_number']

          #if marketing and operating are the same airline
          if segmentJson['marketing_airline']
            segment.marketing_airline_code = segmentJson['marketing_airline']
          elsif
            segment.marketing_airline_code = segmentJson['operating_airline']
          end
          segment.marketing_airline_name =  @airline_code_map[segment.marketing_airline_code]
          segment.marketing_flight_number = segmentJson['flight_number']

          #airplane data stuff i.e. boeing 771
          segment.airplane_data = segmentJson['aircraft']

          #todo cabin class stuff as seat class
          class_travel = get_cabin_class_type( booking_info_data['travel_class'] )
          number_of_seat = booking_info_data["seats_remaining"].to_i

          seats = []
          number_of_seat.times do
            seat = Farandula::Seat.new
            seat.cabin = class_travel
            #amadeus does not indicate the seat place
            seat.place =  ""
            seats << seat
          end

          segment.seats_available = seats

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

          segment

        end

        def get_cabin_class_type (travel_class_info)

          case travel_class_info.downcase
            when "economy"
              CabinClassType::ECONOMY
            when "business"
              CabinClassType::BUSINESS
            when "premiumeconomy"
              CabinClassType::PREMIUM_ECONOMY
            when "first"
              CabinClassType::FIRST
            when "economy/coach"
              CabinClassType::ECONOMYCOACH
            when "premium_economy"
              CabinClassType::PREMIUM_ECONOMY
            else
              CabinClassType::OTHER
          end

        end

      end #ends amadeusflightmanager
    end #ends Amadeus
  end #ends FlightManagers
end # ends Farandula