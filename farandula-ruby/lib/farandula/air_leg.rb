module Farandula
  class AirLeg
      attr_accessor :id
      attr_accessor :departure_airport_code
      attr_accessor :departure_date
      attr_accessor :arrival_airport_code
      attr_accessor :arrival_date
      attr_accessor :segments
      attr_accessor :price

      def initalize(
        id                      = nil,
        departure_airport_code  = nil,
        departure_date          = nil,
        arrival_airport_code    = nil,
        arrival_date            = nil,
        segments                = [],
        price                   = nil
      )
        
        @id                     = id
        @departure_airport_code = departure_airport_code
        @departure_date         = departure_date
        @arrival_airport_code   = arrival_airport_code
        @arrival_date           = arrival_date
        @segments               = segments
        @price                  = price

      end
  end
  
end