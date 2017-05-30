require_relative './constants.rb'

module Farandula
  
  class Passenger

    attr_reader :type, :age
    
    def initialize(type, age)
      @type = type
      @age  = age
      validate_age!
    end  

    def validate_age!
      if @type == PassengerType::ADULTS && @age < 18
        raise AgeValidationError.new
      elsif @type == PassengerType::CHILDREN && (@age > 18 || @age < 3)
        raise AgeValidationError.new
      elsif (@type == PassengerType::INFANTSONSEAT || @type == PassengerType::INFANTS) && @age > 3
        raise AgeValidationError.new
      end
    end

  end
  
end