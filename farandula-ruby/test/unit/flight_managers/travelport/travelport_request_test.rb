require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/travelport/request'
require 'farandula/models/search_form'

class Farandula::TravelportRequestTest < Minitest::Test
  
  include Farandula
  include Farandula::FlightManagers

  def setup
    @request  = Travelport::Request.new
  end

  def test_get_head()
    request_result = @request.get_head('P105356')
    expected =  '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">' + "\n" +
                '  <soapenv:Header/>' + "\n" +
                '  <soapenv:Body>' + "\n" +
                '    <air:LowFareSearchReq xmlns:air="http://www.travelport.com/schema/air_v34_0" AuthorizedBy="user" SolutionResult="true" TargetBranch="P105356" TraceId="trace">' + "\n" +
                '      <com:BillingPointOfSaleInfo xmlns:com="http://www.travelport.com/schema/common_v34_0" OriginApplication="UAPI"/>'

    assert_equal(expected, request_result)
  end

  def test_get_airleg()

    search_form = Farandula::SearchForm.new ["DFW"],
                                                ["CDG"],
                                                [Date.new(2017,07,10)],
                                                [],
                                                {},
                                                :oneway,
                                                :economy,
                                                nil


    airleg_result = @request.get_airlegs search_form ;

    expected =  '<air:SearchAirLeg>' + "\n" +
                '  <air:SearchOrigin>' + "\n" +
                '    <com:Airport Code="DFW" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                '  </air:SearchOrigin>' + "\n" +
                '  <air:SearchDestination>' + "\n" +
                '    <com:Airport Code="CDG" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                '  </air:SearchDestination>' + "\n" +
                '  <air:SearchDepTime PreferredTime="2017-07-10"/>' + "\n" +
                '  <air:AirLegModifiers>' + "\n" +
                '    <air:PreferredCabins>' + "\n" +
                '      <com:CabinClass Type="ECONOMY" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                '    </air:PreferredCabins>' + "\n" +
                '  </air:AirLegModifiers>' + "\n" +
                '</air:SearchAirLeg>'

    assert_equal expected, airleg_result

  end

end