require 'test_helper'
require 'file_helper'
require 'minitest/autorun'

class Farandula::RequestTest < Minitest::Test

  include Farandula::FlightsManagers

  def setup
    @request = Sabre::Request.new
    @json = Jbuilder.new 
  end

  def test_that_build_flight_info_builds_valid_json
    @request.build_flight_info(@json, 1, 'CUU', DateTime.new(2017,04,24), 'SFO')
    expected = FileHelper.load_asset('sabre/flight-info.json')
    assert_equal expected.gsub(/\s+/, ""), @json.target!.gsub(/\s+/, "")
  end

  def test_that_build_header_builds_a_valid_json    
    @request.build_header(@json)
    expected = FileHelper.load_asset('sabre/request-header.json')
    assert_equal expected.gsub(/\s+/, ""), @json.target!.gsub(/\s+/, "")
  end

end