module Farandula
  
  module FlightType
    ONEWAY    = :oneway
    ROUNDTRIP = :roundtrip
    MULTIPLE  = :multiple

    FLIGHT_TYPES = [ONEWAY, ROUNDTRIP, MULTIPLE]
  end 


  module CabinClassType

    ECONOMY         = :economy 
    PREMIUM_ECONOMY = :premium_economy
    FIRST           = :first
    BUSINESS        = :business
    ECONOMYCOACH    = :economycoach
    OTHER           = :other
    TYPES           = [ECONOMY, PREMIUM_ECONOMY, FIRST, BUSINESS ,ECONOMYCOACH, OTHER]
  end

end