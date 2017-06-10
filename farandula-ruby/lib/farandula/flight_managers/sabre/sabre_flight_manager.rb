
require 'rest-client'
require_relative '../flight_manager.rb'
require_relative './access_manager.rb'


module Farandula
  module FlightManagers
    module Sabre

      class SabreFlightManager < FlightManager

        def initialize(creds)
          @access_manager = AccessManager.new(creds[:client_id], creds[:client_secret])
          @target_url = creds[:target_url]
        end 

        def get_avail(search_form) 

          headers = {
            content_type: :json, 
            accept: :json,
            Authorization: @access_manager.build_auth_token
          }

          request = ::Request.new

          response = RestClient.post(
            @target_url,
            request.build_request_for!(search_form),
            headers
          )

          response
        end


        private
        def build_credentials(client_id, client_secret)
          encoded_id     = Base64.strict_encode64(client_id)
          encoded_secret = Base64.strict_encode64(client_secret)
          encoded        = Base64.strict_encode64("#{encoded_id}:#{encoded_secret}")
          "Basic #{encoded}"
        end
        #private ends

      

      end #ends sabreflightmanager
    end #ends Sabre
  end #ends FlightManagers
end # ends Farandula