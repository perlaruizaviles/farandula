module Farandula
  class Itinerary

    attr_accessor :id
    attr_accessor :departure_air_legs
    attr_accessor :returning_air_legs
    attr_accessor :price

    def initialize(
      id = nil,
      departure_air_legs  = nil,
      returning_air_legs  = nil,
      price               = nil
    )

      @id                 = id
      @departure_air_legs = departure_air_legs
      @returning_air_legs = returning_air_legs
      @price              = price

    end

    def to_s
      result = ""
      result << "id #{id}, " \
                "departure_air_legs #{departure_air_legs.to_s}, " \
                "returning_air_legs #{returning_air_legs.to_s}, " \
                "price #{price}."
    end

  end 
end