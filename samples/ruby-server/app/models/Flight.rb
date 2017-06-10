require "date"
require "helpers/DateParser"

class Flight

	def initialize (departureAirport, departureDate, arrivalAirport, segments)
		@departureAirport = departureAirport
		@departureDate = departureDate
		@arrivalAirport = arrivalAirport
		@arrivalDate = arrivalDate
		@segments = segments
	end


	# getter
	def departureAirport
	  @departureAirport
	end

	# setter
	def departureAirport = (value)
	  @departureAirport = value
	end

	def departureDate
	  @departureDate
	end

	#TODO: Value of date / 1000
	def departureDate = (value)
	  @departureDate = DateTime.dateToTimeStampSeconds(value)
	end

	def arrivalAirport
	  @arrivalAirport
	end

	def arrivalAirport = (value)
	  @arrivalAirport = value
	end

	def arrivalDate
	  @arrivalDate
	end

	#TODO: Value of date / 1000
	def arrivalDate = (value)
	  @arrivalDate = DateTime.dateToTimeStampSeconds(value)
	end

	def segments
	  @segments
	end

	def segments = (value)
	  @segments = value
	end


end

