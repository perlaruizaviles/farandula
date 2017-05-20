module Farandula 

  class SearchForm 
  
    attr_accessor :type
    attr_accessor :departure_airport
    attr_accessor :arrival_airport
    attr_accessor :departing_date 
    attr_accessor :returning_date 
    attr_accessor :pasengers
    attr_accessor :cabin_class
    # attr_accessor :offset

    def initialize(
      departure_airport = nil, 
      arrival_airport   = nil, 
      departing_date    = nil, 
      returning_date    = nil, 
      pasengers         = [], 
      type              = :oneway, 
      cabin_class       = :economy)
      # offset            = nil, 

      @type               = type
      @departure_airport  = departure_airport
      @arrival_airport    = arrival_airport
      @departing_date     = departing_date 
      @returning_date     = returning_date 
      @pasengers          = pasengers
      @cabin_class        = cabin_class
      # @offset             = offset
    end 

    def roundtrip?
      @type == :roundtrip
    end

    # Builder helper for Search Form  
    class Builder 
    
      def initialize      
        @search_form = SearchForm.new
      end 

      def from(from) 
        @search_form.departure_airport = from
        self
      end 
      
      def to(to)
        @search_form.arrival_airport= to   
        self 
      end

      def departing_at(departing_at)
        @search_form.departing_date = departing_at
        self
      end

      def returning_at(returning_at)
        @search_form.returning_date = returning_at
        self
      end

      def type(type)
        @search_form.type = type 
        self 
      end 

      #  TODO handle passenger buidling 
      # def for_passengers(passengers) 
      #   @search_form.passengers << passengers
      #   self
      # end

      # def limited_results_to(max_results) 
      #   @search_form.offset = max_results
      #   self
      # end

      def with_cabin_class(cabin_class)
        @search_form.cabin_class = cabin_class
        self
      end

      def build
        @search_form
      end 

    end # Builder ends 
  end  # SearchForm ends
end 
