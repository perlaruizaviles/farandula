require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/amadeus/request'

class Farandula::AmadeusIntegrationTest < Minitest::Test

  include Farandula
  include Farandula::FlightManagers

  def test_that_build_response_from_amadeus_is_valid

    from          = ['CUU']
    to            = ['SFO']
    departing_at  = [DateTime.now + 1]
    returning_at  = [DateTime.now >> 1]

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
                      .limited_results_to( 2 )
                      .build!

    manager = Factory.build_flight_manager(:amadeus, {})
    itineraries = manager.get_avail(search_form)

    puts itineraries[0]

    assert_equal( itineraries[0].air_legs[0].departure_airport_code.downcase , 'cuu' )
    assert_equal( itineraries[0].air_legs[1].departure_airport_code.downcase , 'sfo' )

  end

end
