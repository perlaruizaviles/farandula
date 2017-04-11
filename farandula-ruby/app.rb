require_relative './lib/farandula/trip_manager.rb'

include Farandula

manager = TripManager.new('gato', 'perro')
puts manager.get_avail(nil)

