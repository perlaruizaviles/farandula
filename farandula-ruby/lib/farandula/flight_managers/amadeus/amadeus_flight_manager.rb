require 'rest-client'
require 'yaml'
require_relative '../flight_manager.rb'
require 'farandula/models/itinerary'
require 'farandula/models/air_leg'
require 'farandula/models/segment'
require 'farandula/models/seat'
require 'farandula/models/fares'
require 'farandula/models/price'

require_relative '../../constants.rb'
require 'time'
require 'tzinfo'

module Farandula
  module FlightManagers
    module Amadeus

      class AmadeusFlightManager < FlightManager

        attr_accessor :api_key

        def initialize(api_key)
          @api_key = api_key

          @airline_code_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/amadeus/' + "airlinesCode.yml")

          @locations_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/amadeus/' + "locations.yml")

        end

        def get_avail(search_form)

          request = Amadeus::Request.new

          url_request = request.build_target_url_from_search!(search_form, api_key)

          response = ""
          url_request.each_with_index {| url, index |
            response = RestClient.get url
          }

          build_itineries(response)

        end

        def build_itineries (response)

          itineries_list = []

          parsed = JSON.parse(response)

          itinerary_id = 0

          parsed["results"].each {|result|

            #pricing
            # Map<String, Object> fareMap = (Map<String, Object>) ((LinkedHashMap) result).get("fare");
            # itineraryResult.setPrice(getPrices(fareMap));

            fares = get_prices( result['fare'])

            result['itineraries'].each {|itinerary|

              itineray_result = Farandula::Itinerary.new

              itineray_result.fares = fares

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
            segment_id =+1
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

          segment = Farandula::Segment.new

          segment.key = segment_key

          segment.operating_airline_code = segmentJson['operating_airline']
          segment.operating_airline_name = @airline_code_map[segment.operating_airline_code]
          segment.operating_flight_number = segmentJson['flight_number']

          #if marketing and operating are the same airline
          if segmentJson['marketing_airline']
            segment.marketing_airline_code = segmentJson['marketing_airline']
          elsif segment.marketing_airline_code = segmentJson['operating_airline']
          end
          segment.marketing_airline_name = @airline_code_map[segment.marketing_airline_code]
          segment.marketing_flight_number = segmentJson['flight_number']

          #airplane data stuff i.e. boeing 771
          segment.airplane_data = segmentJson['aircraft']

          # class travel stuff
          class_travel = get_cabin_class_type(booking_info_data['travel_class'])
          number_of_seat = booking_info_data["seats_remaining"].to_i

          seats = []
          number_of_seat.times do
            seat = Farandula::Seat.new
            seat.cabin = class_travel
            #amadeus does not indicate the seat place
            seat.place = ""
            seats << seat
          end
          segment.seats_available = seats

          #departure stuff
          segment.departure_airport_code = departure_airport_data['airport']
          segment.departure_terminal = departure_airport_data['terminal']
          departure_date_time = format_date(Time.parse (segmentJson['departs_at']))
          segment.departure_date = departure_date_time

          #arrival stuff
          segment.arrival_airport_code = arrival_airport_data['airport']
          segment.arrival_terminal = arrival_airport_data['terminal']
          arrival_date_time = format_date (Time.parse(segmentJson['arrives_at']))
          segment.arrival_date = arrival_date_time


          if @locations_map[segment.departure_airport_code]
            departure_location = @locations_map[segment.departure_airport_code]
          end

          if @locations_map[segment.arrival_airport_code]
            arrival_location = @locations_map[segment.arrival_airport_code]
          end

          # duration flight stuff
          if departure_location.equal? arrival_location
            duration = arrival_date_time - departure_date_time
          elsif tz = TZInfo::Timezone.get(departure_location)
            tz2 = TZInfo::Timezone.get(arrival_location)
            # seconds to minutes
            duration = (tz2.local_to_utc(Time.parse(segmentJson['arrives_at'])) -
                tz.local_to_utc(Time.parse (segmentJson['departs_at']))) /60
          end

          segment.duration = duration
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

        def get_prices ( fare )

          fares = Farandula::Fares.new

          fares.base_price = build_price( fare['total_price'] )

          if fare['price_per_adult']
            fares.price_per_adult = build_price( fare['price_per_adult']['total_fare'] )
            fares.tax_per_adult   = build_price( fare['price_per_adult']['tax'] )
          end

          if fare['price_per_child']
            fares.price_per_child = build_price( fare['price_per_child']['total_fare'] )
            fares.tax_per_child   = build_price( fare['price_per_child']['tax'] )
          end

          if fare['price_per_infant']
            fares.price_per_child = build_price( fare['price_per_infant']['total_fare'] )
            fares.tax_per_child   = build_price( fare['price_per_infant']['tax'] )
          end

          fares

        end

        def build_price( pricing_info_data  )

          currency = pricing_info_data.gsub( /[^a-zA-Z]+/, '' )
          if  ''.eql? currency
              currency  = "US"
              amount    = pricing_info_data
          else
              amount = pricing_info_data.gsub( currency, '')
          end

          priceResult = Farandula::Price.new
          priceResult.amount  = amount.to_f
          priceResult.currency_code = currency
          priceResult

        end

        def format_date(date)
          date.strftime('%Y-%m-%dT%H:%M:%S.%L%z')
        end

      end #ends amadeusflightmanager
    end #ends Amadeus
  end #ends FlightManagers
end # ends Farandula