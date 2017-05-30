# myapp.rb
require 'sinatra'
require 'net/http'

get '/api/airports' do
    pattern = params['pattern']
    uri = URI('https://fix-fix-farandula.herokuapp.com/api/airports?pattern='+pattern)
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
    
    request = 'https://fix-fix-farandula.herokuapp.com/api/flights?' +
                'departingAirportCodes=' + departingAirportCodes +
                '&departingDates=' + departingDates +
                '&departingTimes=' + departingTimes +
                '&arrivalAirportCodes=' + arrivalAirportCodes +
                '&type=' + type +
                '&passenger=' + passenger +
                '&cabin=' + cabin
    
    if type == 'round'
        request = request + '&arrivalDate=' + params['arrivalDate'] + 
                            '&arrivalTime=' + params['arrivalTime']
    end
    uri = URI(request)
    
    result = Net::HTTP.get(uri)
    return result
end