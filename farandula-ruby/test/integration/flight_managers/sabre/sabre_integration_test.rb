module Farandula

  class Farandula::SabreIntegrationTest < Minitest::Test

    include Farandula
    include Farandula::FlightManagers

    def test_one_way

      from = ['DFW']
      to = ['CDG']
      departing_at = [DateTime.now + 1]
      returning_at = [DateTime.now >> 1]
      limit = 50

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
          client_secret: 'wLPi0Sy2',
          target_url: 'https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=50&offset=1'
      }
      )

      itineraries = manager.get_avail(search_form)

      assert itineraries.size == 0

    end

  end

end