module FarandulaSample

  class ItineraryFares

    def initialize (base_price = nil, taxes_price= nil, total_price= nil)
      @base_price   = base_price
      @taxes_price  = taxes_price
      @total_price  = total_price
    end

    # getter
    def base_price
      @base_price
    end

    # setter
    def base_price= (value)
      @base_price = value
    end

    def taxes_price
      @taxes_price
    end

    def taxes_price= (value)
      @taxes_price = value
    end

    def total_price
      @total_price
    end

    def total_price= (value)
      @total_price = value
    end

  end
end
