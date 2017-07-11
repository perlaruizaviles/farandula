require 'farandula/constants'

module Farandula 

  class SearchForm 
    
    include Farandula

    attr_accessor :type
    attr_accessor :departure_airport
    attr_accessor :arrival_airport
    attr_accessor :departing_date 
    attr_accessor :returning_date 
    attr_accessor :passengers
    attr_accessor :cabin_class
    attr_accessor :offset

    def initialize(
      departure_airport = [],
      arrival_airport   = [],
      departing_date    = [],
      returning_date    = [],
      passengers        = {}, 
      type              = :oneway, 
      cabin_class       = :economy,
      offset            = nil
    )

      @type               = type
      @departure_airport  = departure_airport
      @arrival_airport    = arrival_airport
      @departing_date     = departing_date 
      @returning_date     = returning_date 
      @passengers         = passengers
      @cabin_class        = cabin_class
      @offset             = offset
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
        @search_form.departure_airport = @search_form.departure_airport + from
        self
      end 
      
      def to(to)
        @search_form.arrival_airport = @search_form.arrival_airport + to
        self 
      end

      def departing_at(departing_at)
        @search_form.departing_date = @search_form.departing_date + departing_at
        self
      end

      def returning_at(returning_at)
        @search_form.returning_date = @search_form.returning_date + returning_at
        self
      end

      def type(type)
        @search_form.type = type 
        self 
      end 

      #  TODO handle passenger buidling 
      def with_passenger(passenger)
        @search_form.passengers[passenger.type] ||= []
        @search_form.passengers[passenger.type] << passenger
        self
      end

      def limited_results_to(max_results)
        @search_form.offset = max_results
        self
      end

      def with_cabin_class(cabin_class)
        @search_form.cabin_class = cabin_class
        self
      end

      def in_enconomy_class
        @search_form.cabin_class = :economy
      end 
      
      def in_businnes_class
        @search_form.cabin_class = :business
      end 
      
      def in_first_class
        @search_form.cabin_class = :first
      end


      def build!(validate = true)
        if validate
          validate!  
        end 
        @search_form
      end 

      protected 
        def validate!
          
          check_not_empty!('departure_airport', @search_form.departure_airport)
          check_not_empty!('arrival_airport', @search_form.arrival_airport)
          check_not_empty!('departing_date', @search_form.departing_date)

          check_not_nil!('type', @search_form.type)

          if @search_form.roundtrip?
            check_not_empty!('returning_date', @search_form.returning_date)
          end 

          if !CabinClassType::TYPES.include?(@search_form.cabin_class)
            raise ValidationError.new("cabin class type [#{@search_form.cabin_class}] not found")
          end 

          if !FlightType::TYPES.include?(@search_form.type)
            raise ValidationError.new("flight type [#{@search_form.type}] not found")
          end 

          valid_departure_date( @search_form.departing_date )

          if @search_form.roundtrip?
            valid_returning_date( @search_form)
          end
        end
        

      private 
        def check_not_nil!(name , obj) 

          if obj.nil?
            raise ValidationError.new "#{name} cannot be nil"
          end 
        end

        def check_not_empty!(name, obj)
          if obj.empty?
            raise ValidationError.new "#{name} cannot be empty"
          end
        end

        def check_is_string (name, obj)
          obj.each {| element|
            if !element.is_a?(String)
              raise ValidationError.new "#{name} has to be of type 'string'"
            end
          }
        end

      def valid_departure_date(departing_dates)
        departing_dates.each {|date_dep|
          if (date_dep <=> DateTime.now) == -1
            raise ValidationError.new("departing_date can\'t be in the pass")
          end
        }
      end

      def valid_returning_date( search_form )

        search_form.departing_date.each_with_index do |element,index|
          if (search_form.departing_date[index] <=> search_form.returning_date[index]) == 1
            raise ValidationError.new("returning_date can't be before departing_date")
          end
        end

      end

    end # Builder ends

    def to_s

      result = ""
      result << "departure_airport #{departure_airport}, " \
                "arrival_airport #{arrival_airport}, " \
                "departing_date #{departing_date}, " \
                "returning_date #{returning_date}, " \
                "type #{type}, " \
                "cabin_class #{cabin_class}," \
                "offset #{offset}." \

    end

  end  # SearchForm ends
end 
