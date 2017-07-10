require_relative '../flightManager.rb'
require 'rest-client'
require 'base64'

module Farandula
  module FlightManagers
    module Travelport
      class TravelportFlightManager < FlightManager

        def self.init
          @airline_code_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/' + "airlinesCode.yml")
          @property_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/travelport/properties/' + "travelportConfig.yml")
          @api_key = @property_map['travelport.api_user']
          @api_password = @property_map['travelport.api_password']
          @target_branch = @property_map['travelport.target_branch']
        end        

        def initialize
          @target_url = 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService'
        end

        def get_avail(search_form)
            data = {
              method: "POST",
              hostname: "americas.universal-api.pp.travelport.com",
              path: "/B2BGateway/connect/uAPI/AirService",
              headers: {
                authorization: "Basic "+get_auth_encoded(),
                content_type: "text/xml"
              }
            }
            
        end
        
        private 
        def get_auth_encoded()
          auth_token = Base64.strict_encode64(@api_key+":"+@api_password)
        end

      end # ends TravelportFlightManager
    end # ends Travelport
  end # ends FlightManagers
end # ends Farandula