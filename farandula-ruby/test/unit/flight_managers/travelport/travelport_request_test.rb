require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/travelport/request'
require 'farandula/models/search_form'
require 'farandula/models/passenger'
require 'farandula/flight_managers/travelport/travelport_flight_manager'


class Farandula::TravelportRequestTest < Minitest::Test
  
  include Farandula
  include Farandula::FlightManagers
  include Farandula::FlightManagers::Travelport

  def setup
    @request  = Travelport::Request.new

    @expected_head =    '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">' + "\n" +
                        '  <soapenv:Header/>' + "\n" +
                        '  <soapenv:Body>' + "\n" +
                        '    <air:LowFareSearchReq xmlns:air="http://www.travelport.com/schema/air_v34_0" AuthorizedBy="user" SolutionResult="true" TargetBranch="P7036596" TraceId="trace">' + "\n" +
                        '      <com:BillingPointOfSaleInfo xmlns:com="http://www.travelport.com/schema/common_v34_0" OriginApplication="UAPI"/>'
  
    @expected_airleg =  '<air:SearchAirLeg>' + "\n" +
                        '  <air:SearchOrigin>' + "\n" +
                        '    <com:Airport Code="DFW" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                        '  </air:SearchOrigin>' + "\n" +
                        '  <air:SearchDestination>' + "\n" +
                        '    <com:Airport Code="CDG" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                        '  </air:SearchDestination>' + "\n" +
                        '  <air:SearchDepTime PreferredTime="2017-07-10"/>' + "\n" +
                        '  <air:AirLegModifiers>' + "\n" +
                        '    <air:PreferredCabins>' + "\n" +
                        '      <com:CabinClass Type="Economy" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                        '    </air:PreferredCabins>' + "\n" +
                        '  </air:AirLegModifiers>' + "\n" +
                        '</air:SearchAirLeg>'

    @expected_airleg_roundtrip =  '<air:SearchAirLeg>' + "\n" +
                                  '  <air:SearchOrigin>' + "\n" +
                                  '    <com:Airport Code="DFW" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                                  '  </air:SearchOrigin>' + "\n" +
                                  '  <air:SearchDestination>' + "\n" +
                                  '    <com:Airport Code="CDG" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                                  '  </air:SearchDestination>' + "\n" +
                                  '  <air:SearchDepTime PreferredTime="2017-07-10"/>' + "\n" +
                                  '  <air:AirLegModifiers>' + "\n" +
                                  '    <air:PreferredCabins>' + "\n" +
                                  '      <com:CabinClass Type="Economy" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
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
                                  '      <com:CabinClass Type="Economy" xmlns:com="http://www.travelport.com/schema/common_v34_0"/>' + "\n" +
                                  '    </air:PreferredCabins>' + "\n" +
                                  '  </air:AirLegModifiers>' + "\n" +
                                  '</air:SearchAirLeg>'

        @expected_passengers =  '<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="ADT" Age="90"/>' + "\n" +
                                '<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="ADT" Age="38"/>' + "\n" +
                                '<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="CHD" Age="4"/>' + "\n" +
                                '<com:SearchPassenger xmlns:com="http://www.travelport.com/schema/common_v34_0" BookingTravelerRef="gr8AVWGCR064r57Jt0+8bA==" Code="CHD" Age="10"/>'
        
        @expected_search_modifier =   '<air:AirSearchModifiers MaxSolutions="50">' + "\n" +
                                      '  <air:PreferredProviders>' + "\n" +
                                      '    <com:Provider xmlns:com="http://www.travelport.com/schema/common_v34_0" Code="1G"/>' + "\n" +
                                      '  </air:PreferredProviders>' + "\n" +
                                      '</air:AirSearchModifiers>'
        
        @expected_tail =  '<air:AirPricingModifiers xmlns:com="http://www.travelport.com/schema/common_v34_0" CurrencyType = "USD"/>' + "\n" +
                          '        </air:LowFareSearchReq>' + "\n" +
                          '        </soapenv:Body>' + "\n" +
                          '        </soapenv:Envelope>'
  end

  def test_get_head()
    actual = @request.get_head('P7036596')
    assert_equal(
      @expected_head,
      actual
    )
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

    actual = @request.get_airlegs search_form
    assert_equal(
      @expected_airleg,
      actual
    )
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

    actual = @request.get_airlegs search_form
    assert_equal(
      @expected_airleg_roundtrip,
      actual
    )
  end

  def test_get_passengers
    passengers_map = {
        :ADULTS     => [Passenger.new(PassengerType::ADULTS, 90), Passenger.new(PassengerType::ADULTS, 38)],
        :CHILDREN   => [Passenger.new(PassengerType::CHILDREN, 4), Passenger.new(PassengerType::CHILDREN, 10)]
    }
    actual = @request.get_passengers passengers_map
    assert_equal(
      @expected_passengers,
      actual
    )
  end

  def test_get_search_modifier()
    actual = @request.get_search_modifier(50)
    assert_equal(
      @expected_search_modifier,
      actual
    )
  end

  def test_get_tail()
    actual = @request.get_tail()
    assert_equal(
      @expected_tail,
      actual
    )
  end

  def test_build_request_for()
    from          = []
    to            = []
    departing_at  = []

    from << 'DFW'
    to << 'CDG'
    departing_at << (Date.new(2017,07,10))

    passenger1  = Passenger.new(:adults, 90)
    passenger2  = Passenger.new(:adults, 38)
    passenger3  = Passenger.new(:children, 4)
    passenger4  = Passenger.new(:children, 10)
    builder     = SearchForm::Builder.new
    search_form = builder
                      .from( from )
                      .to( to )
                      .departing_at( departing_at)
                      .type(:oneway)
                      .with_cabin_class( :economy)
                      .with_passenger( passenger1 )
                      .with_passenger( passenger2 )
                      .with_passenger( passenger3 )
                      .with_passenger( passenger4 )
                      .limited_results_to( 50 )
                      .build!(false)

    
    actual = @request.build_request_for!(search_form)

    expected_request =  @expected_head + "\n" +
                        @expected_airleg + "\n" +
                        @expected_search_modifier + "\n" +
                        @expected_passengers + "\n" +
                        @expected_tail

    assert_equal(
      expected_request,
      actual
    )
  end

  def test_get_avail
    from          = []
    to            = []
    departing_at  = []

    from << 'DFW'
    to << 'CDG'
    departing_at << ((Date.today >> 1))

    passenger1  = Passenger.new(:adults, 90)
    passenger2  = Passenger.new(:adults, 38)
    passenger3  = Passenger.new(:children, 4)
    passenger4  = Passenger.new(:children, 10)
    builder     = SearchForm::Builder.new
    search_form = builder
                      .from( from )
                      .to( to )
                      .departing_at( departing_at)
                      .type(:oneway)
                      .with_cabin_class( :economy)
                      .with_passenger( passenger1 )
                      .with_passenger( passenger2 )
                      .with_passenger( passenger3 )
                      .with_passenger( passenger4 )
                      .limited_results_to( 2 )
                      .build!(false)
    flight_manager = TravelportFlightManager.new
    result = flight_manager.get_avail search_form
    refute_nil result, 'Response returned'
    assert flight_manager.flight_details.length > 0
  end

  def test_check_fares
    response = File.read(File.dirname(__FILE__) + '/../../../assets/travelport/response.xml')
    flight_manager = TravelportFlightManager.new
    itinerary_list = flight_manager.parse_response response

    actual_total = itinerary_list[0].fares.total_price.amount
    expected_total = 6422.40

    actual_base = itinerary_list[0].fares.base_price.amount
    expected_base = 5554.00

    actual_taxes = itinerary_list[0].fares.taxes_price.amount
    expected_taxes = 868.40

    actual_total_currency = itinerary_list[0].fares.total_price.currency_code
    actual_base_currency = itinerary_list[0].fares.base_price.currency_code
    actual_taxes_currency = itinerary_list[0].fares.taxes_price.currency_code
    expected_currency_code = "USD"

    assert_equal(expected_total, actual_total)
    assert_equal(expected_base, actual_base)
    assert_equal(expected_taxes, actual_taxes)

    assert_equal(expected_currency_code, actual_total_currency)
    assert_equal(expected_currency_code, actual_base_currency)
    assert_equal(expected_currency_code, actual_taxes_currency)
  end

  def test_available_seats
    response = File.read(File.dirname(__FILE__) + '/../../../assets/travelport/response.xml')
    flight_manager = TravelportFlightManager.new
    itinerary_list = flight_manager.parse_response response
    actual_seat = itinerary_list[0].air_legs[0].segments[0].seats_available[0]

    assert_equal('Economy', actual_seat.cabin)

  end

end