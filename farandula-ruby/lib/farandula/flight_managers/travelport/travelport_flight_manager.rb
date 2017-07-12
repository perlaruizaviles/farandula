require_relative '../flight_manager.rb'
require 'rest-client'
require 'nokogiri'
require_relative 'request'
require_relative 'travelport_flight_details'

module Farandula
  module FlightManagers
    module Travelport
      class TravelportFlightManager < FlightManager

        Farandula::FlightManagers::Travelport

        attr_reader :flight_details

        def initialize
          @target_url = 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService'
          @airline_code_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/' + "airlinesCode.yml")
          @logger = Logger.new File.new('farandula-ruby.log', 'w')
          @logger.level = Logger::DEBUG

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

          fill_flight_details! Nokogiri::XML(response).remove_namespaces!

          #puts @flight_details

          #TODO: implement logger
          response

        end

        private

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