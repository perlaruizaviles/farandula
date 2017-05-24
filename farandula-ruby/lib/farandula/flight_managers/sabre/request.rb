require 'jbuilder'

module Farandula
  module FlightsManagers
    module Sabre
      class Request
        
        def format_date(date)
          date.strftime('%FT%T')
        end 

        def build_request_for!(search_form)
          @json = Jbuilder.new   
          @json.OTA_AirLowFareSearchRQ do
            build_header(@json)
            build_destination_information(@json, search_form)
            build_travel_preferences(@json, search_form.cabin_class)
            build_travel_info_summary(@json, search_form.passengers.size)
            build_tpa_extensions(@json)
          end

          @json.target!
        end   

        def build_travel_preferences(json, cabin)
          json.TravelPreferences do 
            json.ValidInterlineTicket true
            json.CabinPref do 
              json.array! [ 1 ] do |_|
                json.Cabin cabin
                json.PreferLevel 'Preferred'
              end 
            end 

            json.TPA_Extensions do 
              json.TripType do 
                json.Value 'Return'
              end 

              json.LongConnectTime do 
                json.Min 780
                json.Max 1200
                json.Enable true
              end 

              json.ExcludeCallDirectCarriers do 
                json.Enabled true
              end
              
            end
          end 
        end 

        def build_destination_information(json, search_form) 
          elements = [{
            origin: search_form.departure_airport, 
            destination: search_form.arrival_airport, 
            date: search_form.departing_date
            }
          ]
          
          if search_form.roundtrip?
            elements << {
              origin: search_form.arrival_airport, 
              destination: search_form.departure_airport, 
              date: search_form.returning_date
            }
          end 
          # TODO: handle :multiple

          json.OriginDestinationInformation do 
            json.array! elements.each_with_index.to_a do |(element, idx)|
              build_flight_info(
                json, 
                (idx + 1).to_s, 
                element[:origin], 
                element[:date], 
                element[:destination]
              )
            end 
          end 
        end 
        

        def build_flight_info(json, id, departing_airport, departing_date, destination_airport) 
          json.RPH id.to_s
          json.DepartureDateTime format_date(departing_date)
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

        def build_travel_info_summary(json, numberOfPassengers)
          json.TravelerInfoSummary do 
            json.SeatsRequested do 
              json.array! [ numberOfPassengers ]
            end 

            json.AirTravelerAvail do 
              json.array! [ 1 ] do |_|
                json.PassengerTypeQuantity do 
                  json.array! [ 1 ] do |_|
                    json.Code 'ADT'
                    json.Quantity numberOfPassengers
                  end 
                end 
              end 
            end 
          end
        end 

        def build_tpa_extensions(json)
          json.TPA_Extensions do 
            json.IntelliSellTransaction do 
              json.RequestType do 
                json.Name '50ITINS'
              end 
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