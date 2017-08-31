module FarandulaSample
  class FlightItinerary

    def initialize (key, type, flights, fares)
      @key      = key
      @type     = type
      @airlegs  = flights
      @fares    = fares
    end

    # getter
    def key
      @key
    end

    # setter
    def key=(value)
      @key = value
    end

    def type
      @type
    end

    def type=(value )
      @type = value
    end

    def airlegs
      @airlegs
    end

    def airlegs=(value )
      @airlegs = value
    end

    def fares
      @fares
    end

    def fares=(value )
      @fares = value
    end

  end
end
