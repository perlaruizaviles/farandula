module Farandula
  module FlightManagers
    module Amadeus

      class Request

        def format_date(date)
          date.strftime('%Y-%d-%m')
        end

        def build_url_request_for!( search_form, api_key )

          numberOfResults = "&number_of_results=#{search_form.offset}"

          unless search_form.passengers[:adults].nil?
            passengersData = "&adults=#{search_form.passengers[:adults].size }"
          end

          unless search_form.passengers[:children].nil?
            passengersData += "&children=#{search_form.passengers[:children].size }"
          end

          unless search_form.passengers[:infants].nil?
            passengersData += "&infants=#{search_form.passengers[:infants].size }"
          end

          cabin = "&travel_class=#{search_form.cabin_class}"
          origin =  "&origin=#{search_form.departure_airport}"
          destination = "&destination=#{search_form.arrival_airport}"
          departingDateSearch = format_date( search_form.departing_date )
          date = "&departure_date=#{departingDateSearch}"

          apiURL = "https://api.sandbox.amadeus.com/v1.2/flights/" \
                    "low-fare-search?apikey=#{api_key}" \
                    "#{cabin}" \
                    "#{origin}" \
                    "#{destination}" \
                    "#{date}" \
                    "#{passengersData}" \
                    "#{numberOfResults}"

          apiURL

        end

      end



    end # Amadeus ends
  end # FlightManagers ends
end # Farandula ends
