module Farandula
  class Itinerary

    attr_accessor :id
    attr_accessor :air_legs
    attr_accessor :fares

    def initialize(
      id = nil,
      air_legs  = [],
      fares     = nil
    )

      @id       = id
      @air_legs = air_legs
      @fares    = fares

    end

    def to_s
      result = ""
      result << "id #{id}, " \
                "air_legs #{air_legs.to_s}, " \
                "fares #{fares}."
    end

  end 
end