
module Farandula
  class Error < StandardError; end
  class UnauthorizedError < Error; end
  class FlightManagerNotImplementedError < Error; end
end