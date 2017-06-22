
class ItineraryFares

	def initialize (basePrice, taxesPrice, totalPrice)
		@basePrice = basePrice
		@taxesPrice = taxesPrice
		@totalPrice = totalPrice
	end


	# getter
	def basePrice
	  @basePrice
	end

	# setter
	def basePrice = (value)
	  @basePrice = value
	end

	def taxesPrice 
	  @taxesPrice
	end

	def taxesPrice = (value)
	  @taxesPrice = value
	end

	def totalPrice
	  @totalPrice
	end

	def totalPrice = (value)
	  @totalPrice = value
	end

end