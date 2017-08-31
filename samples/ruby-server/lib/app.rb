require 'sinatra'
require 'net/http'
require 'active_support/all'
require 'jbuilder'
require 'farandula'
require_relative 'helpers/assets'
require_relative 'helpers/builders'
require_relative 'models/flight_itinerary'
require_relative '../lib/helpers/flight_helper'

include FarandulaSample

builder = Builders.new

get '/airports' do

  response['Access-Control-Allow-Origin'] = '*'

  query = params[:pattern].downcase

  builder.hashCities.select do |value|
    (value.name.include? query) || (value.city.include? query) || (value.iata.include? query)
  end.to_a.to_json

end

get '/flights' do

  response['Access-Control-Allow-Origin'] = '*'

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

  ItineraryHelper.get_flight_itinerary_from_itinerary( manager.get_avail(search_form), search_form.type ).to_json

end
