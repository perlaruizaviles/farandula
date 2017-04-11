
require 'rest-client'

require_relative './access_manager.rb'

module Farandula

	class TripManager 

		def initialize(client_id, client_secret)
			@access_manager = AccessManager.new(client_id, client_secret)
		end 


		def get_avail(search_command) 
			headers = {
				content_type: :json, 
				accept: :json,
				Authorization: @access_manager.build_auth_token
			}

			response = RestClient.post(
				"https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=50&offset=1",
				json_request,
				headers
			)

			response
		end 

		def json_request
		 '{
				 "OTA_AirLowFareSearchRQ": {
				     "Target": "Production",
				       "POS": {
				            "Source": [{
				                "PseudoCityCode":"F9CE",
				                "RequestorID": {
				                    "Type": "1",
				                  "ID": "1",
				                    "CompanyName": {
				                        
				                  }
				             }
				         }]
				        },
				        "OriginDestinationInformation": [{
				          "RPH": "1",
				           "DepartureDateTime": "2017-07-07T11:00:00",
				           "OriginLocation": {
				             "LocationCode": "DFW"
				         },
				            "DestinationLocation": {
				                "LocationCode": "CDG"
				         },
				            "TPA_Extensions": {
				             "SegmentType": {
				                    "Code": "O"
				               }
				         }
				     },
				        {
				         "RPH": "2",
				           "DepartureDateTime": "2017-07-08T11:00:00",
				           "OriginLocation": {
				             "LocationCode": "CDG"
				         },
				            "DestinationLocation": {
				                "LocationCode": "DFW"
				         },
				            "TPA_Extensions": {
				             "SegmentType": {
				                    "Code": "O"
				               }
				         }
				     }],
				       "TravelPreferences": {
				          "ValidInterlineTicket": true,
				           "CabinPref": [{
				             "Cabin": "Y",
				             "PreferLevel": "Preferred"
				            }],
				           "TPA_Extensions": {
				             "TripType": {
				                   "Value": "Return"
				             },
				                "LongConnectTime": {
				                    "Min": 780,
				                 "Max": 1200,
				                    "Enable": true
				              },
				                "ExcludeCallDirectCarriers": {
				                  "Enabled": true
				             }
				         }
				     },
				        "TravelerInfoSummary": {
				            "SeatsRequested": [1],
				          "AirTravelerAvail": [{
				              "PassengerTypeQuantity": [{
				                 "Code": "ADT",
				                    "Quantity": 1
				               }]
				            }]
				        },
				        "TPA_Extensions": {
				         "IntelliSellTransaction": {
				             "RequestType": {
				                    "Name": "50ITINS"
				             }
				         }
				     }
				 }
			}'
		end 

	end
end