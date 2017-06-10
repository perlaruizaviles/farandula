module Farandula
  class AirLeg
      attr_accessor :id
      attr_accessor :departure_airport_code
      attr_accessor :departing_date
      attr_accessor :arrival_airport_code
      attr_accessor :arrival_date
      attr_accessor :segments
      attr_accessor :price

      def initalize(
        id,
        departure_airport_code,
        departing_date,
        arrival_airport_code,
        arrival_date,
        segments = [], 
        price) 
        
        @id                     = id
        @departure_airport_code = departure_airport_code
        @departing_date         = departing_date
        @arrival_airport_code   = arrival_airport_code
        @arrival_date           = arrival_date
        @segments               = segments
        @price                  = price

      end
  end
  
end