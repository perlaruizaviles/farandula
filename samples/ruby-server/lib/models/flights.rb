require "date"

module FarandulaSample

  class Flight

    def date_to_times_stamp_seconds(date)
      return date
    end

    def initialize (departure_airport= nil, departure_date= nil, arrival_airport= nil, arrival_date=nil, segments= nil)
      @departure_airport  = departure_airport
      @departure_date     = departure_date
      @arrival_airport    = arrival_airport
      @arrival_date       = arrival_date
      @segments           = segments
    end

    # getter
    def departure_airport
      @departure_airport
    end

    # setter
    def departure_airport= (value)
    @departure_airport = value
    end

    def departure_date
      @departure_date
    end

    def departure_date= (value)
      @departure_date = DateTime.date_to_times_stamp_seconds(value)
    end

    def arrival_airport
      @arrival_airport
    end

    def arrival_airport= (value)
      @arrival_airport = value
    end

    def arrival_date
      @arrival_date
    end

    def arrival_date= (value)
      @arrival_date = DateTime.date_to_times_stamp_seconds(value)
    end

    def segments
      @segments
    end

    def segments= (value)
      @segments = value
    end

  end
end
