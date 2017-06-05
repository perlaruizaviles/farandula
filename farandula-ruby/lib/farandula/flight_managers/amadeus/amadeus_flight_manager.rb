require 'rest-client'
require_relative '../flight_manager.rb'

module Farandula
  module FlightManagers
    module Amadeus

      class AmadeusFlightManager < FlightManager

        attr_accessor :api_key

        def initialize( api_key )
          @api_key = api_key
        end 

        def get_avail(search_form)

          request = Amadeus::Request.new

          url_request = request.build_url_request_for!( search_form, api_key )

          #puts url_request

          response = RestClient.get url_request

        end

      end #ends amadeusflightmanager
    end #ends Amadeus
  end #ends FlightManagers
end # ends Farandula