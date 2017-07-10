require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/travelport/request'

class Farandula::TravelportRequestTest < Minitest::Test
  
  include Farandula
  include Farandula::FlightManagers

  def setup
    @request  = Travelport::Request.new
  end

  def test_get_head()
    request_result = @request.get_head('P105356')
    expected = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
  <soapenv:Header/>
  <soapenv:Body>
    <air:LowFareSearchReq xmlns:air="http://www.travelport.com/schema/air_v34_0" AuthorizedBy="user" SolutionResult="true" TargetBranch="P105356" TraceId="trace">
      <com:BillingPointOfSaleInfo xmlns:com="http://www.travelport.com/schema/common_v34_0" OriginApplication="UAPI"/>'
    assert_equal(request_result, expected)
  end

end