require 'erb'
module Farandula
  module FlightManagers
    module Travelport
      class Request
      
        def format_date(date)
          date.strftime('%Y-%m-%d')
        end

        def get_head(target_branch)
          str = File.read(File.dirname(__FILE__)+'/../../assets/travelport/requestHeader.xml')
          #result = eval("\""+str+"\"")



        end

        def replace_string(file, params)
          params
          template = ""
          open(file) { |f|
            template = f.to_a.join
          }

          updated = ERB.new(template, 0, "%<>").result
        end

      end
    end
  end
end