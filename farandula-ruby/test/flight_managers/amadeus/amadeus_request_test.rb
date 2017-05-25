require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/amadeus/request'

class Farandula::AmadeusRequestTest < Minitest::Test

  include Farandula
  include Farandula::FlightManagers
  
  def setup
    @request = Amadeus::Request.new
  end 


  def test_perro
    a = "tomas"
    @request.hellou( a )

    assert_equal(
        a,
        a
    )

  end


end