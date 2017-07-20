module Farandula

  class Farandula::SabreIntegrationTest < Minitest::Test

    include Farandula
    include Farandula::FlightManagers

    def test_one_way

      from = ['DFW']
      to = ['CDG']
      departing_at = [DateTime.now + 1]
      returning_at = [DateTime.now >> 1]
      limit = 2

      passenger = Passenger.new(:adults, 25)
      builder = SearchForm::Builder.new
      search_form = builder
                        .from(from)
                        .to(to)
                        .departing_at(departing_at)
                        .returning_at(returning_at)
                        .type(:oneway)
                        .with_cabin_class(:economy)
                        .with_passenger(passenger)
                        .limited_results_to(limit)
                        .build!

      manager = Factory.build_flight_manager(:sabre, {
            client_id: 'V1:zej6hju9ltib108l:DEVCENTER:EXT',
            client_secret: 'wLPi0Sy2'
          }
      )

      itineraries = manager.get_avail(search_form)

      puts itineraries[0].air_legs[0]

      assert itineraries.size <= limit
      assert_equal itineraries[0].air_legs[0].departure_airport_code, from[0]
      assert_equal itineraries[0].air_legs[0].arrival_airport_code, to[0]
      assert_equal itineraries[0].air_legs.size , 1

    end

  end

end