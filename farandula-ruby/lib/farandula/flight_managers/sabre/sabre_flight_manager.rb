
require 'rest-client'
require_relative '../flight_manager.rb'
require_relative './access_manager.rb'


module Farandula
  module FlightManagers
    module Sabre

      class SabreFlightManager < FlightManager

        def initialize(creds)
          @access_manager = AccessManager.new(creds[:client_id], creds[:client_secret] )
          # maps
          @airline_code_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/amadeus/' + "airlinesCode.yml")
          @airline_cabin_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/sabre/' + "cabins.yml")

        end 

        def get_avail(search_form) 

          headers = {
            content_type: :json, 
            accept: :json,
            Authorization: @access_manager.build_auth_token
          }

          request = Sabre::Request.new

          response = RestClient.post(
            'https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=50&offset=1',
            request.build_request_for!(search_form),
            headers
          )

          build_itineries( response, search_form  )

          # response
        end


        private
        def build_credentials(client_id, client_secret)
          encoded_id     = Base64.strict_encode64(client_id)
          encoded_secret = Base64.strict_encode64(client_secret)
          encoded        = Base64.strict_encode64("#{encoded_id}:#{encoded_secret}")
          "Basic #{encoded}"
        end

        def build_itineries( response , search_command )

            itineraries_list = []

        end

        #private ends

      

      end #ends sabreflightmanager
    end #ends Sabre
  end #ends FlightManagers
end # ends Farandula