require 'jbuilder'

module Farandula
  module FlightsManagers
    module Sabre
      class Request
        
        def build_request_for!(search_form)
          @json = Jbuilder.new   
          @json.OTA_AirLowFareSearchRQ do
            build_header(@json)
            build_destination_information(@json, search_form)
          end

          @json.target!
        end   

        def build_destination_information(json, search_form) 
          elements = [{origin: search_form.departure_airport, destination: search_form.arrival_airport, date: search_form.departing_date}]
          
          if search_form.roundtrip?
            elements << {origin: search_form.arrival_airport, destination: search_form.departure_airport, date: search_form.returning_date}
          end 
          # TODO: handle :multiple

            json.OriginDestinationInformation do 
              json.array! elements do |e|
                build_flight_info(json, 1, search_form.departure_airport, search_form.departing_date, search_form.arrival_airport)
              end 
            end 
        end 
        

        def build_flight_info(json, id, departing_airport, departing_date, destination_airport) 
          json.RPH id.to_s
          json.DepartureDateTime departing_date
          json.OriginLocation do 
            json.LocationCode departing_airport
          end 

          json.DestinationLocation do 
            json.LocationCode destination_airport
          end 

          json.TPA_Extensions do 
            json.SegmentType do 
              json.Code 'O'
            end 
          end

        end 

        def build_header(json)
            
          json.Target 'Production'
          json.POS do 
            json.Source do 
              json.array! [ 1 ] do |_|
                json.PseudoCityCode 'F9CE'
                json.RequestorID do 
                  json.Type  '1'
                  json.ID  '1'
                  json.set! :CompanyName, {}
                end
              end 
            end 
          end

        end 
 
      end 

    end # Sabre ends
  end # FlightManagers ends
end # Farandula ends

# arr = ['a', 'b', 'c']
# j.PseudoCityCode "F9CE",
#         j.RequestorID  {
#           j.Type  "1",
#           j.ID  "1",
#           j.CompanyName  {

#           }
#         }



# {
#   "OTA_AirLowFareSearchRQ": {
#     "Target": "Production",
#     "POS": {
#       "Source": [{
#         "PseudoCityCode":"F9CE",
#         "RequestorID": {
#           "Type": "1",
#           "ID": "1",
#           "CompanyName": {

#           }
#         }
#       }]
#     },
#     "OriginDestinationInformation": [{
#       "RPH": "1",
#       "DepartureDateTime": "${departingDate}",
#       "OriginLocation": {
#         "LocationCode": "${departureAirport}"
#       },
#       "DestinationLocation": {
#         "LocationCode": "${arrivalAirport}"
#       },
#       "TPA_Extensions": {
#         "SegmentType": {
#           "Code": "O"
#         }
#       }
#     },
#       {
#         "RPH": "2",
#         "DepartureDateTime": "${returningDate}",
#         "OriginLocation": {
#           "LocationCode": "${arrivalAirport}"
#         },
#         "DestinationLocation": {
#           "LocationCode": "${departureAirport}"
#         },
#         "TPA_Extensions": {
#           "SegmentType": {
#             "Code": "O"
#           }
#         }
#       }],
#     "TravelPreferences": {
#       "ValidInterlineTicket": true,
#       "CabinPref": [{
#         "Cabin": "${classTravel}",
#         "PreferLevel": "Preferred"
#       }],
#       "TPA_Extensions": {
#         "TripType": {
#           "Value": "Return"
#         },
#         "LongConnectTime": {
#           "Min": 780,
#           "Max": 1200,
#           "Enable": true
#         },
#         "ExcludeCallDirectCarriers": {
#           "Enabled": true
#         }
#       }
#     },
#     "TravelerInfoSummary": {
#       "SeatsRequested": [${passengersNumber}],
#       "AirTravelerAvail": [{
#         "PassengerTypeQuantity": [{
#           "Code": "ADT",
#           "Quantity": ${passengersNumber}
#         }]
#       }]
#     },
#     "TPA_Extensions": {
#       "IntelliSellTransaction": {
#         "RequestType": {
#           "Name": "50ITINS"
#         }
#       }
#     }
#   }
# }