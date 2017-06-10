require "date"
require "helpers/DateParser"

class FlightSegment

	def initialize (departureAirport, departureDate, arrivalAirport, arrivalDate, duration, airLineMarketingName, airLineOperationName, airplaneData, cabinTypes)
		@departureAirport = departureAirport
    	@departureDate = departureDate 
    	@arrivalAirport = arrivalAirport
    	@arrivalDate = arrivalDate
    	@duration = duration

    	@airLineMarketingName = airLineMarketingName
    	@airLineOperationName = airLineOperationName
    	@airplaneData = airplaneData

    	@cabinTypes = cabinTypes
    end


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

	def duration
	  @duration
	end

	def duration = (value)
	  @duration = value
	end

	def airLineMarketingName
	  @airLineMarketingName
	end

	def airLineMarketingName = (value)
	  @airLineMarketingName = value
	end

	def airLineOperationName
	  @airLineOperationName
	end

	def airLineOperationName = (value)
	  @airLineOperationName = value
	end

	def airplaneData
	  @airplaneData
	end

	def airplaneData = (value)
	  @airplaneData = value
	end

	def cabinTypes
	  @cabinTypes
	end

	def cabinTypes = (value)
	  @cabinTypes = value
	end


end



