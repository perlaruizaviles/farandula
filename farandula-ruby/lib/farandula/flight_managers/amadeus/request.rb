module Farandula
  module FlightManagers
    module Amadeus

      class Request

        def format_date(date)
          date.strftime('%Y-%m-%d')
        end

        def build_url_request_for!( search_form, api_key )

          number_of_results = "&number_of_results=#{search_form.offset}"

          unless search_form.passengers[:adults].nil?
            passengers_data = "&adults=#{search_form.passengers[:adults].size }"
          end

          unless search_form.passengers[:children].nil?
            passengers_data += "&children=#{search_form.passengers[:children].size }"
          end

          unless search_form.passengers[:infants].nil?
            passengers_data += "&infants=#{search_form.passengers[:infants].size }"
          end

          cabin = "&travel_class=#{search_form.cabin_class}"
          origin =  "&origin=#{search_form.departure_airport}"
          destination = "&destination=#{search_form.arrival_airport}"
          departing_date_search = format_date( search_form.departing_date )
          date = "&departure_date=#{departing_date_search}"

          apiURL = "https://api.sandbox.amadeus.com/v1.2/flights/" \
                    "low-fare-search?apikey=#{api_key}" \
                    "#{cabin}" \
                    "#{origin}" \
                    "#{destination}" \
                    "#{date}" \
                    "#{passengers_data}" \
                    "#{number_of_results}"

          apiURL

        end

      end



    end # Amadeus ends
  end # FlightManagers ends
end # Farandula ends
