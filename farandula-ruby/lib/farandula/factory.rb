require_relative './flight_managers/sabre_flight_manager.rb'

module Farandula
  module Factory
    include FlightManagers

    def self.build_flight_manager(service, credentials) 
      case service
      when :sabre 
        SabreFlightManager.new(credentials)
      else 
        raise FlightManagerNotImplementedError.new
      end
    end 

  end 
end

