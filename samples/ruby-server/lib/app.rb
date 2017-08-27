require 'sinatra'
require 'net/http'
require 'active_support/all'
require 'farandula'
require_relative 'helpers/assets'
require_relative 'helpers/builders'

include Farandula_sample

builder = Builders.new

get '/city_to_code' do

  query = params[:city].downcase
  builder.hashCities.select { |key, value|
    value.city.include? query
  }.to_json

end

get '/get_flights' do

  departingAirportCodes = params['departingAirportCodes']
  departingDates        = params['departingDates']
  departingTimes        = params['departingTimes']
  arrivalAirportCodes   = params['arrivalAirportCodes']
  type                  = params['type']
  passenger             = params['passenger']
  cabin                 = params['cabin']

  from = ['DFW']
  to = ['CDG']
  departing_at = [DateTime.now + 1]
  limit = 2
  passenger = Farandula::Passenger.new(:adults, 25)
  builder = Farandula::SearchForm::Builder.new
  search_form = builder
                    .from(from)
                    .to(to)
                    .departing_at(departing_at)
                    .type(:oneway)
                    .with_cabin_class(:economy)
                    .with_passenger(passenger)
                    .limited_results_to(limit)
                    .build!

  manager = Farandula::Factory.build_flight_manager(:sabre, {
      client_id: 'V1:zej6hju9ltib108l:DEVCENTER:EXT',
      client_secret: 'wLPi0Sy2'

  })

  manager.get_avail(search_form).to_json

end
