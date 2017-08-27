module Farandula_sample

  class City
    attr_accessor :name
    attr_accessor :city
    attr_accessor :country
    attr_accessor :iata

    def initialize(
        name = nil,
        city = nil,
        country = nil,
        iata = nil
    )
      @name     = name
      @city     = city
      @country  = country
      @iata     = iata

    end

  end

end
