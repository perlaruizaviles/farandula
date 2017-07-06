require 'test_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/amadeus/request'

class Farandula::AmadeusIntegrationTest < Minitest::Test

  include Farandula
  include Farandula::FlightManagers

  def test_round_way_trip

    from          = ['CUU']
    to            = ['SFO']
    departing_at  = [DateTime.now + 1]
    returning_at  = [DateTime.now >> 1]
    limit = 10

    passenger   = Passenger.new(:adults, 25)
    builder     = SearchForm::Builder.new
    search_form = builder
                      .from(from)
                      .to(to)
                      .departing_at(departing_at  )
                      .returning_at(returning_at )
                      .type(:roundtrip)
                      .with_cabin_class(:economy)
                      .with_passenger( passenger )
                      .limited_results_to( 10 )
                      .build!

    manager = Factory.build_flight_manager(:amadeus, {})
    itineraries = manager.get_avail(search_form)

    assert itineraries.size <= limit
    #this one checks round-trip
    assert itineraries[0].air_legs.size == 2
    assert_equal( itineraries[0].air_legs[0].departure_airport_code.downcase , 'cuu' )
    assert_equal( itineraries[0].air_legs[1].departure_airport_code.downcase , 'sfo' )

  end

end
