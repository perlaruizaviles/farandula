require 'json'

def airportParser
	file = File.read('file1.json')
	airportsHash = JSON.parse(file)

	  airportsIataHash = Hash.new 
  
  airportsHash['airports'].each do |airport|
    airportsIataHash[airport['iata']] = airport
  end

	return airportsIataHash
end

airportsHashMap = airportParser()