module Farandula
  module FlightManagers
    module Amadeus

      class Request

        def format_date(date)
          date.strftime('%Y-%m-%d')
        end

        def build_target_url_from_search!( search_form, api_key )

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
          returning_date_search = format_date( search_form.returning_date )
          departure_date = "&departure_date=#{departing_date_search}"
          returning_date = "&return_date=#{returning_date_search}"

          if search_form.type == :roundtrip

            apiURL = "https://api.sandbox.amadeus.com/v1.2/flights/" \
                      "low-fare-search?apikey=#{api_key}" \
                      "#{cabin}" \
                      "#{origin}" \
                      "#{destination}" \
                      "#{departure_date}" \
                      "#{returning_date}" \
                      "#{passengers_data}" \
                      "#{number_of_results}"

          elsif
            apiURL = "https://api.sandbox.amadeus.com/v1.2/flights/" \
                      "low-fare-search?apikey=#{api_key}" \
                      "#{cabin}" \
                      "#{origin}" \
                      "#{destination}" \
                      "#{departure_date}" \
                      "#{passengers_data}" \
                      "#{number_of_results}"

          end

          apiURL

        end

      end



    end # Amadeus ends
  end # FlightManagers ends
end # Farandula ends
