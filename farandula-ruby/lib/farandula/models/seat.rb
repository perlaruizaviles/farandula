module Farandula
  
  class Seat

    attr_accessor :cabin, :place

    def initialize(
        cabin = nil,
        place = nil
    )
      @cabin  = cabin
      @place  = place
    end 

    def to_s
      result = ""
      result << "cabin #{cabin}, place code #{place}."
    end

  end  

end
