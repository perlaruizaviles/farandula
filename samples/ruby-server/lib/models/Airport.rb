module FarandulaSample
  class Airport

    def initialize (airportName= nil, city=nil, country=nil, iata=nil)
      @airportName  = airportName
      @city         = city
      @country      = country
      @iata         = iata
    end

    # getter
    def airportName
      @airportName
    end

    # setter
    def airportName=(value)
      @airportName= value
    end

    def city
      @city
    end

    def city=(value)
      @city = value
    end

    def country
      @country
    end

    def country=(value)
      @country = value
    end

    def iata
      @iata
    end

    def iata=(value)
      @iata = value
    end

  end
end
