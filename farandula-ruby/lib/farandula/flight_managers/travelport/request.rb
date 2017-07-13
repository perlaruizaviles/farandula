require 'base64'
module Farandula
  module FlightManagers
    module Travelport
      class Request


        def initialize
          @property_map = YAML.load_file(File.dirname(__FILE__) + '/../../assets/travelport/properties/' + "travelportConfig.yml")
          @api_key = @property_map['travelport.api_user']
          @api_password = @property_map['travelport.api_password']
          @target_branch = @property_map['travelport.target_branch']
        end

        def format_date(date)
          date.strftime('%Y-%m-%d')
        end

        def get_headers
          {
              Authorization: "Basic #{get_auth_encoded}",
              content_type: 'text/xml',
              accept: 'text/xml'
          }
        end


        def build_request_for!(search_form)
          get_head(@target_branch) + "\n" +
          get_airlegs(search_form) + "\n" +
          get_search_modifier(search_form.offset) + "\n" +
          get_passengers(search_form.passengers) + "\n" +
          get_tail()
        end

        def get_head(target_branch)
          str = File.read(File.dirname(__FILE__)+'/../../assets/travelport/requestHeader.xml')
          str = replace_string(str, {target_branch: target_branch})
          str.gsub!("\r", "")
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
          result.gsub!("\r", "")
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
          str = result.join("\n")
        end

        def get_search_modifier(limit)
          str = File.read(File.dirname(__FILE__) + '/../../assets/travelport/searchModifier.xml')
          str = replace_string(str, {limit: limit})
          str.gsub!("\r", "")
        end

        def get_tail
          str = File.read(File.dirname(__FILE__) + '/../../assets/travelport/requestTail.xml')
          str.gsub!("\r","")
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

          def get_auth_encoded
            Base64.encode64("#{@api_key}:#{@api_password}").gsub("\n",'')
          end
      end
    end
  end
end