module Farandula
  class Itinerary

    attr_accessor :id
    attr_accessor :departure_air_legs
    attr_accessor :returning_air_legs
    attr_accessor :fares

    def initialize(
      id = nil,
      departure_air_legs  = nil,
      returning_air_legs  = nil,
      fares               = nil
    )

      @id                 = id
      @departure_air_legs = departure_air_legs
      @returning_air_legs = returning_air_legs
      @fares              = fares

    end

    def to_s
      result = ""
      result << "id #{id}, " \
                "departure_air_legs #{departure_air_legs.to_s}, " \
                "returning_air_legs #{returning_air_legs.to_s}, " \
                "fares #{fares}."
    end

  end 
end