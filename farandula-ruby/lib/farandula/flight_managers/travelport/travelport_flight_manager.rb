require_relative '../flight_manager.rb'
require 'rest-client'
require 'nokogiri'
require_relative 'request'
require_relative '../../utils/logger_utils'
require_relative '../../models/segment'
require_relative '../../models/itinerary'
require_relative '../../models/air_leg'
require_relative 'travelport_flight_details'
require_relative '../../models/price'
require_relative '../../models/fares'
require_relative '../../models/seat'

module Farandula
  module FlightManagers
    module Travelport
      class TravelportFlightManager < FlightManager

        include Farandula
        include Farandula::Utils
        include Farandula::FlightManagers::Travelport
        attr_reader :flight_details


        def initialize
          @target_url = 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService'
          @airline_code_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/' + "airlinesCode.yml")
          @logger = Logger.new File.new('farandula-ruby.log', File::WRONLY | File::APPEND | File::CREAT)
          @logger.level = Logger::DEBUG
          @segments_map = {}
          @flight_details = {}

        end

        def get_avail(search_form)
          request = Request.new

          body = request.build_request_for! search_form
          @logger.debug("REQUEST \n #{LoggerUtils.get_pretty_xml(body)} \n END REQUEST \n")

          headers = request.get_headers
          response = RestClient.post(
              @target_url,
              body,
              headers
          )
          @logger.debug ("RESPONSE \n #{LoggerUtils.get_pretty_xml(response)} \n END RESPONSE \n")
          parse_response response
        end

        def parse_response response
          xml_response = Nokogiri::XML(response).remove_namespaces!
          fill_flight_details! xml_response
          fill_flight_segments! xml_response
          fill_flight_itineraries! xml_response
        end

        def fill_flight_itineraries! response
          solution_node_list = response.xpath('//AirPricingSolution')
          itinerary_list = solution_node_list.each_with_index.map do |solution, solution_index|
            get_seats solution
            itinerary          = Itinerary.new
            itinerary.id       = solution_index
            itinerary.fares    = parse_fare solution
            itinerary.air_legs = solution.xpath('Journey').each_with_index.map do |journey, journey_index|
              airleg = AirLeg.new
              airleg.segments               = journey.xpath('AirSegmentRef').map do |segment_ref|
                                                @segments_map[segment_ref.attr('Key').to_s]
              end
              airleg.id                     = journey_index
              airleg.departure_airport_code = airleg.segments.first.departure_airport_code
              airleg.departure_date         = airleg.segments.first.departure_date
              airleg.arrival_airport_code   = airleg.segments.last.arrival_airport_code
              airleg.arrival_date           = airleg.segments.last.arrival_date
              airleg
            end
            itinerary
          end
          itinerary_list
        end

        def parse_price price_string
          currency  = price_string[0..2]
          amount    = price_string[3..-1]
          Price.new(amount.to_f, currency)
        end

        def parse_fare solution_node
          total_price_string  = solution_node.attr('TotalPrice').to_s if solution_node.to_s.include? 'TotalPrice'
          base_price_string   = solution_node.attr('BasePrice').to_s if solution_node.to_s.include? 'BasePrice'
          taxes_string        = solution_node.attr('Taxes').to_s if solution_node.to_s.include? 'Taxes'
          Fares.new(parse_price(base_price_string),
                    parse_price(taxes_string),
                    parse_price(total_price_string))
        end

        def get_seats solution_node

          pricing_info = solution_node.xpath('AirPricingInfo').first
          booking_info_list = pricing_info.xpath('BookingInfo')

          booking_info_list.each do |booking|
            booking_count = booking.attr('BookingCount').to_i
            cabin_class   = booking.attr('CabinClass').to_s
            booking_count.times do
              seat = Seat.new(cabin_class, '')
              @segments_map[booking.attr('SegmentRef').to_s].seats_available << seat
            end
          end
        end

        def fill_flight_segments! response
          segment_node_list = response.xpath('//AirSegment')
          segment_array = segment_node_list.map do |segment_node|
            segment = Segment.new
            code_share_info = segment_node.xpath('CodeshareInfo')
            flight_details_key = segment_node.xpath('FlightDetailsRef').attr('Key').to_s
            segment.key = segment_node.attr('Key').to_s
            segment.marketing_airline_code = segment_node.attr('Carrier').to_s
            segment.operating_airline_code = code_share_info.attr('OperatingCarrier').to_s if code_share_info.to_s.include? 'OperatingCarrier'
            segment.operating_flight_number = code_share_info.attr('OperatingFlightNumber').to_s if code_share_info.to_s.include? 'OperatingFlightNumber'
            segment.marketing_airline_name = code_share_info.to_s.include? 'OperatingCarrier' ? @airline_code_map[segment.marketing_airline_code] : code_share_info.text
            segment.operating_airline_name = @airline_code_map[segment.operating_airline_code]
            segment.airplane_data = @flight_details[flight_details_key].equipment
            segment.departure_terminal = @flight_details[flight_details_key].origin_terminal
            segment.arrival_terminal = @flight_details[flight_details_key].destination_terminal
            segment.marketing_flight_number = segment_node.attr('FlightNumber').to_s
            segment.departure_airport_code = segment_node.attr('Origin').to_s
            segment.departure_date = Time.parse segment_node.attr('DepartureTime').to_s
            segment.arrival_airport_code = segment_node.attr('Destination').to_s
            segment.arrival_date = segment_node.attr('ArrivalTime').to_s
            segment.duration = segment_node.attr('FlightTime').to_s
            [segment.key , segment]
          end
          @segments_map = segment_array.to_h
        end

        def fill_flight_details!( response )
          detail_list = response.xpath('//FlightDetails')
          detail_array = detail_list.map do |fd_node|
            attrs = fd_node.attributes
            current = TravelportFlightDetails.new(
                attrs['Key'].to_s,
                attrs['OriginTerminal'].to_s,
                attrs['DestinationTerminal'].to_s,
                attrs['FlightTime'].to_s,
                attrs['Equipment'].to_s,
                nil
            )
            [current.key, current]
          end
          @flight_details = detail_array.to_h
        end
      end # ends TravelportFlightManager
    end # ends Travelport
  end # ends FlightManagers
end # ends Farandula
