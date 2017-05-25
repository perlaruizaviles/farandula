require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'

class Farandula::SabreRequestTest < Minitest::Test

  include Farandula
  include Farandula::FlightManagers
  
  def setup
    @request = Sabre::Request.new
    @json    = Jbuilder.new 
  end

  def test_that_build_flight_info_builds_valid_json
    @request.build_flight_info(@json, 1, 'CUU', DateTime.new(2017,04,24), 'SFO')
    expected = FileHelper.load_asset('sabre/flight-info.json')
    assert_equal(
      StringHelper.no_space(expected),
      StringHelper.no_space(@json.target!)
    )
  end

  def test_that_build_header_builds_a_valid_json    
    @request.build_header(@json)
    expected = FileHelper.load_asset('sabre/request-header.json')
    assert_equal(
      StringHelper.no_space(expected),
      StringHelper.no_space(@json.target!)
    )
  end

  def test_that_build_travel_preferences_builds_valid_json
    @request.build_travel_preferences(@json, 'B')
    expected = FileHelper.load_asset('sabre/travel-preferences.json')
    assert_equal(
      StringHelper.no_space(expected),
      StringHelper.no_space(@json.target!)
    )
  end

  def test_that_build_travel_info_summary_builds_valid_json
    @request.build_travel_info_summary(@json, 2)
    expected = FileHelper.load_asset('sabre/travel-info-summary.json')
    assert_equal(
      StringHelper.no_space(expected),
      StringHelper.no_space(@json.target!)
    )
  end 

  def test_that_build_tpa_extensions_builds_valid_josn
    @request.build_tpa_extensions(@json)
    expected = FileHelper.load_asset('sabre/tpa-extensions.json')    
    assert_equal(
      StringHelper.no_space(expected),
      StringHelper.no_space(@json.target!)
    )
  end 

  def test_that_build_request_for_builds_valid_json 
    builder     = SearchForm::Builder.new
    search_form = builder
                    .from('CUU')
                    .to('SFO')
                    .departing_at(DateTime.new(2017,12,24))
                    .returning_at(DateTime.new(2017,12,30))
                    .type(:roundtrip)
                    .with_cabin_class('Y')
                    .with_passenger({name: 'Daniel'})
                    .build!

    expected    = FileHelper.load_asset('sabre/request.json')
    actual      = @request.build_request_for!(search_form) 

    assert_equal( 
      StringHelper.no_space(expected),
      StringHelper.no_space(actual)
    )

  end 


end