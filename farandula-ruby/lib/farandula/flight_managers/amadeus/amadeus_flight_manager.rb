require 'rest-client'
require_relative '../flight_manager.rb'

module Farandula
  module FlightManagers
    module Amadeus

      class AmadeusFlightManager < FlightManager

        attr_accessor :apiKey

        def initialize( apiKey )
          @apiKey = apiKey
        end 

        def get_avail(search_form)

          request = Amadeus::Request.new
          response = request.build_url_request_for!( search_form, @apiKey )

        end

      end #ends amadeusflightmanager
    end #ends Amadeus
  end #ends FlightManagers
end # ends Farandula