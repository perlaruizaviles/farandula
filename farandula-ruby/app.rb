
# require_relative './lib/farandula/factory.rb'
# manager = Farandula::Factory.build_flight_manager(:sabre, {
#   client_id: 'V1:zej6hju9ltib108l:DEVCENTER:EXT', 
#   client_secret: 'wLPi0Sy2',
#   target_url: 'https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=50&offset=1'
#   }
# )
# puts manager.get_avail(nil)


require_relative './lib/farandula/flight_managers/sabre/request.rb'
require_relative './lib/farandula/search_form.rb'

include Farandula
include Farandula::FlightsManagers::Sabre

builder = SearchForm::Builder.new
builder.type(:roundtrip)
req = Request.new
puts req.build_request_for!(builder.build)