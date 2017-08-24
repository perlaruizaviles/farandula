# myapp.rb
require 'sinatra'
require 'net/http'

include Farandula

serverUrl = 'https://new-farandula.herokuapp.com'
roundTripTag = 'roundTrip'

attr_accessor :hashCities

def initialize
    file        = Farandula::Assets.load_file('airports.json')
    parsed      = JSON.parse(file)
    @hashCities = Hash.new
    @hashCities = parsed["airports"].map { |airport| [airport['iata'].downcase, build_city(airport)] }.to_h
end


get '/city_to_code' do
    query = params[:city].downcase
    init.hashCities.select { |key, value|   value.city.include? query   }.to_json
end

get '/api/airports' do
    pattern = params['pattern']
    uri = URI(serverUrl+'/api/airports?pattern='+pattern)
    return Net::HTTP.get(uri)
end

get '/api/flights' do
    departingAirportCodes = params['departingAirportCodes']
    departingDates = params['departingDates']
    departingTimes = params['departingTimes']
    arrivalAirportCodes = params['arrivalAirportCodes']
    type = params['type']
    passenger = params['passenger']
    cabin = params['cabin']
    
    request = serverUrl+'/api/flights?' +
                'departingAirportCodes=' + departingAirportCodes +
                '&departingDates=' + departingDates +
                '&departingTimes=' + departingTimes +
                '&arrivalAirportCodes=' + arrivalAirportCodes +
                '&type=' + type +
                '&passenger=' + passenger +
                '&cabin=' + cabin
    
    if type == roundTripTag
        request = request + '&returnDates=' + params['returnDates'] + 
                            '&returnTimes=' + params['returnTimes']
    end
    uri = URI(request)
    
    result = Net::HTTP.get(uri)
    return result
end