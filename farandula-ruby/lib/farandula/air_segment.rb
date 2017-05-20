module Farandula
  class AirSegment
    attr_accessor :key # String 
    attr_accessor :airline_icon_path # String 
    attr_accessor :operating_airline_code # String 
    attr_accessor :operating_irline_name # String 
    attr_accessor :operating_flight_number # String 
    attr_accessor :marketing_airline_code # String 
    attr_accessor :marketing_airline_name # String 
    attr_accessor :marketing_flight_number # String 

    attr_accessor :departure_airport_code # String 
    attr_accessor :departure_terminal # String 
    attr_accessor :departure_date # LocalDateTime 

    attr_accessor :arrival_airport_ode # String 
    attr_accessor :arrival_terminal # String 
    attr_accessor :arrival_date # LocalDateTime 

    attr_accessor :airplane_data # String 
    attr_accessor :duration # long 
    attr_accessor :seats_available # List<Seat> 
    attr_accessor :price # Fares 

    def initialize(
      key,
      airline_icon_path,
      operating_airline_code,
      operating_irline_name,
      operating_flight_number,
      marketing_airline_code,
      marketing_airline_name,
      marketing_flight_number,
      departure_airport_code,
      departure_terminal,
      departure_date,
      arrival_airport_ode,
      arrival_terminal,
      arrival_date,
      airplane_data,
      duration,
      seats_available,
      price)

      @key                      = key
      @airline_icon_path        = airline_icon_path
      @operating_airline_code   = operating_airline_code
      @operating_irline_name    = operating_irline_name
      @operating_flight_number  = operating_flight_number
      @marketing_airline_code   = marketing_airline_code
      @marketing_airline_name   = marketing_airline_name
      @marketing_flight_number  = marketing_flight_number
      @departure_airport_code   = departure_airport_code
      @departure_terminal       = departure_terminal
      @departure_date           = departure_date
      @arrival_airport_ode      = arrival_airport_ode
      @arrival_terminal         = arrival_terminal
      @arrival_date             = arrival_date
      @airplane_data            = airplane_data
      @duration                 = duration
      @seats_available          = seats_available
      @price                    = price

    end 

  end 
  
end