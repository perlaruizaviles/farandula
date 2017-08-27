require_relative '../../lib/models/'
module Farandula_sample
  class Builders

    attr_reader :hashCities

    def initialize
      file        = Farandula_sample::Assets.load_file('airports.json')
      parsed      = JSON.parse(file)
      @hashCities = parsed["airports"].map { |airport| [airport['iata'].downcase, build_city(airport)] }.to_h
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
