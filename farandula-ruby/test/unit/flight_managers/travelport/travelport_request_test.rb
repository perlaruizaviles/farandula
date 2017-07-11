require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/travelport/request'
require 'farandula/models/search_form'
require 'farandula/models/passenger'

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


    airleg_result = @request.get_airlegs search_form

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

  def test_airleg_build_roundtrip()

    search_form = Farandula::SearchForm.new ["DFW"],
                                            ["CDG"],
                                            [Date.new(2017,7,10)],
                                            [Date.new(2017,8,10)],
                                            [],
                                            :roundtrip,
                                            :economy,
                                            nil

    airleg_result = @request.get_airlegs search_form

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
        '</air:SearchAirLeg>' +
        '<air:SearchAirLeg>' + "\n" +
        '  <air:SearchOrigin>' + "\n" +
        '    <com:Airport Code="CDG" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
        '  </air:SearchOrigin>' + "\n" +
        '  <air:SearchDestination>' + "\n" +
        '    <com:Airport Code="DFW" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
        '  </air:SearchDestination>' + "\n" +
        '  <air:SearchDepTime PreferredTime="2017-08-10"/>' + "\n" +
        '  <air:AirLegModifiers>' + "\n" +
        '    <air:PreferredCabins>' + "\n" +
        '      <com:CabinClass Type="ECONOMY" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
        '    </air:PreferredCabins>' + "\n" +
        '  </air:AirLegModifiers>' + "\n" +
        '</air:SearchAirLeg>'

    assert_equal expected, airleg_result

  end

  def test_get_passengers
    passengers_map = {
        :ADULTS     => [Passenger.new(PassengerType::ADULTS, 90), Passenger.new(PassengerType::ADULTS, 38)],
        :CHILDREN   => [Passenger.new(PassengerType::CHILDREN, 4), Passenger.new(PassengerType::CHILDREN, 10)]
    }
    expected = '<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="ADT" Age="90"/>' + "\n" +
        '<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="ADT" Age="38"/>' + "\n" +
        '<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="CHD" Age="4"/>' + "\n" +
        '<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="CHD" Age="10"/>'

    result = @request.get_passengers passengers_map
    
    assert_equal expected, result
  end

  def test_get_search_modifier()
    expected =  '<air:AirSearchModifiers MaxSolutions="50">' + "\n" +
                '  <air:PreferredProviders>' + "\n" +
                '    <com:Provider xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="1G"/>' + "\n" +
                '  </air:PreferredProviders>' + "\n" +
                '</air:AirSearchModifiers>'

    actual = @request.get_search_modifier(50)

    assert_equal(expected, actual)
  end

end