module Farandula
  module FlightManagers
    module Amadeus

      class Request

        def format_date(date)
          date.strftime('%Y-%d-%m')
        end

        def build_url_request_for!( search_form, apiKey )

          #hard coded things
          #apiKey = "R6gZSs2rk3s39GPUWG3IFubpEGAvUVUA"
          passengersData = "&adults=1"
          numberOfResults = "&number_of_results=2"

          cabin = "&travel_class=#{search_form.cabin_class}"
          origin =  "&origin=#{search_form.departure_airport}"
          destination = "&destination=#{search_form.arrival_airport}"
          departingDateSearch = format_date( search_form.departing_date )
          date = "&departure_date=#{departingDateSearch}"

          apiURL = "https://api.sandbox.amadeus.com/v1.2/flights/" \
                    "low-fare-search?apikey=#{apiKey}" \
                    "#{cabin}" \
                    "#{origin}" \
                    "#{destination}" \
                    "#{date}" \
                    "#{passengersData}" \
                    "#{numberOfResults}" \

          apiURL
        end

      end



    end # Amadeus ends
  end # FlightManagers ends
end # Farandula ends
