module Farandula
  class Itinerary

    attr_accessor :departure_air_legs
    attr_accessor :returning_air_legs
    attr_accessor :price

    def initialize(
      departure_air_legs,
      returning_air_legs,
      price)
    
      @departure_air_legs = departure_air_legs
      @returning_air_legs = returning_air_legs
      @price              = price

    end
  end 
end