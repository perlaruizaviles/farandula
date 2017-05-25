module Farandula
  module FlightManagers
    module Amadeus
      
      class Request
        
        def format_date(date)
          date.strftime('%FT%T')
        end

        def hellou( str )
          puts str
        end

        def build_flight_info(url, id, departing_airport, departing_date, destination_airport)
         puts url
        end

      end



    end # Amadeus ends
  end # FlightManagers ends
end # Farandula ends