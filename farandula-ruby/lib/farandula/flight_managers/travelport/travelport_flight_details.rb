module Farandula
  module FlightManagers
    module Travelport
      class TravelportFlightDetails
        attr_reader :key,
                    :origin_terminal,
                    :destination_terminal,
                    :flight_time,
                    :equipment

        attr_accessor :group

        def initialize(
          key                  = nil,
          origin_terminal      = nil,
          destination_terminal = nil,
          flight_time          = nil,
          equipment            = nil,
          group                = nil
        )

          @key                  = key
          @origin_terminal      = origin_terminal
          @destination_terminal = destination_terminal
          @flight_time          = flight_time
          @equipment            = equipment
          @group                = group
        end

        def to_s
          "Key: #{@key},"\
          "Origin terminal: #{@origin_terminal},"\
          "Destination terminal: #{@destination_terminal},"\
          "Flight time: #{@flight_time},"\
          "Equipment: #{@equipment},"\
          "Group: #{@group}"
        end
      end
    end
  end
end
