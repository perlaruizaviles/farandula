require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'

class Farandula::RequestTest < Minitest::Test

  include Farandula::FlightsManagers
  
  def setup
    @request = Sabre::Request.new
    @json    = Jbuilder.new 
  end

  def test_that_build_flight_info_builds_valid_json
    @request.build_flight_info(@json, 1, 'CUU', DateTime.new(2017,04,24), 'SFO')
    expected = FileHelper.load_asset('sabre/flight-info.json')
    assert_equal StringHelper.no_space(expected), StringHelper.no_space(@json.target!)
  end

  def test_that_build_header_builds_a_valid_json    
    @request.build_header(@json)
    expected = FileHelper.load_asset('sabre/request-header.json')
    assert_equal StringHelper.no_space(expected), StringHelper.no_space(@json.target!)
  end

  def test_that_build_travel_preferences_builds_valid_json
    @request.build_travel_preferences(@json, 'B')
    expected = FileHelper.load_asset('sabre/travel-preferences.json')
    assert_equal StringHelper.no_space(expected), StringHelper.no_space(@json.target!)
  end

  def test_that_build_travel_info_summary_builds_valid_json
    @request.build_travel_info_summary(@json, 2)
    expected = FileHelper.load_asset('sabre/travel-info-summary.json')
    assert_equal StringHelper.no_space(expected), StringHelper.no_space(@json.target!)
  end 

  def test_that_build_tpa_extensions_builds_valid_josn
    @request.build_tpa_extensions(@json)
    
  end 


end