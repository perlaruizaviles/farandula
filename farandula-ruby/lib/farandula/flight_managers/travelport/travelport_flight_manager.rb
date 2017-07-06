require_relative '../flightManager.rb'
require 'rest-client'

module Farandula
  module FlightManagers
    module Travelport
      class TravelportFlightManager < FlightManager
        def initialize
          @target_url = 'https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService'
        end

        def get_avail(search_form)
          # do stuff
            data = {
              method: "POST",
              hostname: "americas.universal-api.pp.travelport.com",
              path: "/B2BGateway/connect/uAPI/AirService",
              headers: {
                authorization: "Basic VW5pdmVyc2FsIEFQSS91QVBJLTY1NjUyMzc4ODpuRWRlS1lFZ2EyZmdCdGFBajg0RXJmSjlr",
                content_type: "text/xml"
              }
            }
            
        end

      end # ends TravelportFlightManager
    end # ends Travelport
  end # ends FlightManagers
end # ends Farandula