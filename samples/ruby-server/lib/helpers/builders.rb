require_relative '../../lib/models/city'

module FarandulaSample

  class Builders

    attr_reader :hashCities

    def initialize
      file        = FarandulaSample::Assets.load_file('airports.json')
      parsed      = JSON.parse(file)
      @hashCities = parsed["airports"].map { |airport| build_city(airport) }
    end

    def build_city(airport)
      city         = City.new
      city.name    =  airport["name"].downcase
      city.city    =  airport["city"].downcase
      city.country =  airport["country"].downcase
      city.iata    =  airport["iata"].downcase
      city
    end

  end
end
