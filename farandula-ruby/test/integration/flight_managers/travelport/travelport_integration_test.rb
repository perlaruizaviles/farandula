require 'test_helper'
require 'minitest/autorun'
require 'farandula/flight_managers/travelport/request'

class Farandula::TravelportIntegrationTest < Minitest::Test
  include Farandula
  include Farandula::FlightManagers

  def test_one_way
    from          = ['CUU']
    to            = ['SFO']
    departing_at  = [Date.today >> 1]
    limit = 10

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
                      .limited_results_to( limit )
                      .build!

    flight_manager = Factory.build_flight_manager(:travelport, {})
    itineraries = flight_manager.get_avail search_form
    assert itineraries.size <= limit

    assert itineraries[0].air_legs.size == 1
    assert_equal( itineraries[0].air_legs[0].departure_airport_code.downcase , 'cuu' )
    assert_equal( itineraries[0].air_legs[0].arrival_airport_code.downcase , 'sfo' )
  end

  def test_round_trip
    from          = ['CDG']
    to            = ['DFW']
    departing_at  = [DateTime.now + 1]
    returning_at  = [DateTime.now >> 1]
    limit = 10

    passenger1  = Passenger.new(:adults, 90)
    passenger2  = Passenger.new(:adults, 38)
    passenger3  = Passenger.new(:children, 4)
    passenger4  = Passenger.new(:children, 10)
    passenger5  = Passenger.new(:infantsonseat, 2)
    builder     = SearchForm::Builder.new
    search_form = builder
                      .from( from )
                      .to( to )
                      .departing_at( departing_at)
                      .returning_at( returning_at )
                      .type(:roundtrip)
                      .with_cabin_class( :economy)
                      .with_passenger( passenger1 )
                      .with_passenger( passenger2 )
                      .with_passenger( passenger3 )
                      .with_passenger( passenger4 )
                      .with_passenger( passenger5 )
                      .limited_results_to( limit )
                      .build!

    flight_manager = Factory.build_flight_manager(:travelport, {})
    itineraries = flight_manager.get_avail search_form
    assert itineraries.size <= limit
    #this one checks round-trip
    assert itineraries[0].air_legs.size == 2
    assert_equal( itineraries[0].air_legs[0].departure_airport_code.downcase , 'cdg' )
    assert_equal( itineraries[0].air_legs[1].departure_airport_code.downcase , 'dfw' )
  end

  def test_open_jaw
    from          = ['DFW','MEX','HMO']
    to            = ['CDG','LAS','LAX']
    departing_at  = [DateTime.now + 1, DateTime.now + 11, DateTime.now + 21 ]
    returning_at  = [DateTime.now + 10, DateTime.now + 20, DateTime.now >>1 ]
    limit = 50

    passenger   = Passenger.new(:adults, 25)
    builder     = SearchForm::Builder.new
    search_form = builder
                      .from(from)
                      .to(to)
                      .departing_at(departing_at  )
                      .returning_at(returning_at )
                      .type(:openjaw)
                      .with_cabin_class(:economy)
                      .with_passenger( passenger )
                      .limited_results_to( limit )
                      .build!

    manager = Factory.build_flight_manager(:travelport, {})
    itineraries = manager.get_avail(search_form)
    assert itineraries.size <= limit
    #this one checks openjaw
    assert itineraries[0].air_legs.size == from.size
    assert_equal( itineraries[0].air_legs[0].departure_airport_code.downcase , from[0].downcase )
  end

  def test_passenger_validation
    from          = ['CDG']
    to            = ['DFW']
    departing_at  = [DateTime.now + 1]
    returning_at  = [DateTime.now >> 1]
    limit = 10

    passenger  = Passenger.new(:adults, 90)
    builder     = SearchForm::Builder.new
    search_form = builder
                      .from( from )
                      .to( to )
                      .departing_at( departing_at)
                      .returning_at( returning_at )
                      .type(:roundtrip)
                      .with_cabin_class( :economy)
                      .with_passenger( passenger )
                      .with_passenger( passenger )
                      .with_passenger( passenger )
                      .with_passenger( passenger )
                      .with_passenger( passenger )
                      .with_passenger( passenger )
                      .with_passenger( passenger )
                      .with_passenger( passenger )
                      .limited_results_to( limit )

    assert_raises(SearchFormFormatError) {search_form.build!}
  end
end
