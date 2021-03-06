# myapp.rb
require 'sinatra'
require 'net/http'

serverUrl = 'https://new-farandula.herokuapp.com'
roundTripTag = 'roundTrip'

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