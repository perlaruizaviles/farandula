require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'

class Farandula::RequestTest < Minitest::Test

  include Farandula
  include Farandula::FlightsManagers

  def setup_amadeus
    @request = Amadeus::Request.new
  end

  def test_that_build_flight_info_builds_valid_url

    #@url_request = "2"
    #@request.build_flight_info( @url_request, 1, 'CUU', DateTime.new(2017,04,24), 'SFO')

    a = "tomas"
    @request.hellou( a )

    assert_equal(
        a,
        a
    )

  end


end