module Farandula
  module FlightManagers
    module Travelport
      class Request
      
        def format_date(date)
          date.strftime('%Y-%m-%d')
        end

        def get_head(target_branch)
          str = File.read(File.dirname(__FILE__)+'/../../assets/travelport/requestHeader.xml')
          replace_string str, {target_branch: target_branch}
        end

        def get_airlegs(search_form)

          str = File.read(File.dirname(__FILE__)+'/../../assets/travelport/requestSearchAirleg.xml')

          result = ""

          search_form.departure_airport.each_with_index {|leg, i|

            map = {
                departure_airport:  search_form.departure_airport[i],
                arrival_airport:    search_form.arrival_airport[i],
                departure_date:     search_form.departing_date[i],
                class_travel:       search_form.cabin_class.upcase
            }

            result += replace_string str, map

            result += replace_string str, {
                  departure_airport:  search_form.arrival_airport[i],
                  arrival_airport:    search_form.departure_airport[i],
                  departure_date:     search_form.returning_date[i],
                  class_travel:       search_form.cabin_class.upcase
            } if search_form.roundtrip?

          }

          result

        end

        def get_passengers(passengers={})
          passengers_xml = File.read(File.dirname(__FILE__)+'/../../assets/travelport/requestPassenger.xml')

          result = passengers.each_key.map { |type|

            passengers[type].each.map { |passenger|
              map = {
                  passenger_type: (get_travelport_passenger_code passenger.type),
                  passenger_age:  passenger.age
              }
              replace_string passengers_xml, map

            }
          }
          result.join("\n")
        end

        private
          def replace_string(file, params)
            file % params
          end

          def get_travelport_passenger_code(passenger_type)
            case passenger_type
              when PassengerType::ADULTS
                'ADT'
              when PassengerType::CHILDREN
                'CHD'
              when PassengerType::INFANTSONSEAT
                'INS'
              when PassengerType::INFANTS
                'INF'
              else
                'ADT'
            end
          end

      end
    end
  end
end