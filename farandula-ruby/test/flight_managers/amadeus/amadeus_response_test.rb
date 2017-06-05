require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/amadeus/request'

class Farandula::AmadeusResponseTest < Minitest::Test

  include Farandula
  include Farandula::FlightManagers

  def test_that_build_response_from_amadeus_is_valid

    passenger   = Passenger.new(:adults, 25)
    builder     = SearchForm::Builder.new
    search_form = builder
                      .from('CUU')
                      .to('SFO')
                      .departing_at(DateTime.new(2017,12,24))
                      .returning_at(DateTime.new(2017,12,30))
                      .type(:roundtrip)
                      .with_cabin_class( :economy)
                      .with_passenger( passenger )
                      .limited_results_to( 2 )
                      .build!

    expectedResponse = FileHelper.load_asset('amadeus/response.json')
    manager = Factory.build_flight_manager(:amadeus, {})
    result = manager.get_avail(search_form)

    assert_equal( result.code , 200 )
    assert_equal( expectedResponse , result.body )

  end

end
