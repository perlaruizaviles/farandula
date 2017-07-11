require_relative '../flight_manager.rb'
require 'rest-client'
require_relative 'request'

module Farandula
  module FlightManagers
    module Travelport
      class TravelportFlightManager < FlightManager

        def initialize
          @target_url = 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService'
          @airline_code_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/' + "airlinesCode.yml")
          @logger = Logger.new File.new('farandula-ruby.log', 'w')
          @logger.level = Logger::DEBUG
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

          #TODO: implement logger
          response

        end

      end # ends TravelportFlightManager
    end # ends Travelport
  end # ends FlightManagers
end # ends Farandula