
require_relative './lib/farandula/factory.rb'

manager = Farandula::Factory.build_flight_manager(:sabre, {client_id: 'V1:zej6hju9ltib108l:DEVCENTER:EXT', client_secret: 'wLPi0Sy2'})

puts manager.get_avail(nil)

