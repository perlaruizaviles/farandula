require_relative '../flight_manager.rb'
require 'rest-client'
require 'nokogiri'
require_relative 'request'
require_relative '../../utils/logger_utils'
require_relative '../../models/segment'
require_relative '../../models/itinerary'
require_relative '../../models/air_leg'
require_relative 'travelport_flight_details'

module Farandula
  module FlightManagers
    module Travelport
      class TravelportFlightManager < FlightManager


        include Farandula
        include Farandula::Utils
        Farandula::FlightManagers::Travelport
        attr_reader :flight_details


        def initialize
          @target_url = 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService'
          @airline_code_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/' + "airlinesCode.yml")
          @logger = Logger.new File.new('farandula-ruby.log', 'w')
          @logger.level = Logger::DEBUG
          @segments_map = {}
          @flight_details = {}

        end

        def get_avail(search_form)
          request = Request.new

          body = request.build_request_for! search_form
          #TODO: implement logger

          headers = request.get_headers
          response = RestClient.post(
              @target_url,
              body,
              headers
          )



          #puts @flight_details

          #TODO: implement logger
          puts "RESPONSE \n #{LoggerUtils.get_pretty_xml response, 2} \n END RESPONSE \n"
          parse_response response
          response
        end

        def parse_response response
          xml_response = Nokogiri::XML(response).remove_namespaces!
          fill_flight_details! xml_response
          fill_flight_segments! xml_response
          fill_flight_itineraries! xml_response

        end

        def fill_flight_itineraries! response
          solution_node_list = response.xpath('//AirPricingSolution')
          itinerary_list = solution_node_list.map { |solution|

            itinerary = Itinerary.new
            itinerary.air_legs = solution.xpath('Journey').map do |journey|
              airleg = AirLeg.new
              airleg.segments = journey.xpath('AirSegmentRef').map do |segment_ref|
                @segments_map[segment_ref.attr('Key').to_s]
              end
              #TODO: Autoincrement ID
              airleg.id                     = '0'
              #
              airleg.departure_airport_code = airleg.first.departure_airport_code
              airleg.departure_date         = airleg.first.departure_date
              airleg.arrival_airport_code   = airleg.last.arrival_airport_code
              airleg.arrival_date           = airleg.last.arrival_date
              airleg
            end
            itinerary
          }
          puts "HERE IS THE LIST: \n#{itinerary_list}\n LIST END"
          itinerary_list
        end

        def fill_flight_segments! response
          #TODO: Refactor segment setting properties
          segment_node_list = response.xpath('//AirSegment')
          segment_node_list.each do |segment_node|

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
            @segments_map[segment.key] = segment
            puts segment
          end
          puts @segments_map
        end



        def fill_flight_details!( response )

          detail_list = response.xpath("//FlightDetails")

          detail_list.each do |fd_node|

            attrs = fd_node.attributes

            current = TravelportFlightDetails.new(
                attrs["Key"].to_s,
                attrs["OriginTerminal"].to_s,
                attrs["DestinationTerminal"].to_s,
                attrs["FlightTime"].to_s,
                attrs["Equipment"].to_s,
                nil
            )

            @flight_details[current.key] = current

          end

        end

      end # ends TravelportFlightManager
    end # ends Travelport
  end # ends FlightManagers
end # ends Farandula