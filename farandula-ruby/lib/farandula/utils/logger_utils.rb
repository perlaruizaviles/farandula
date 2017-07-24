require 'json'

module Farandula
  module Utils
    class LoggerUtils
      def self.get_pretty_json(ugly_json_string)
        json_object = JSON.parse(ugly_json_string)
        JSON.pretty_generate(json_object)
      end

      def self.get_pretty_xml(input)
        Nokogiri::XML(input).to_xml
      end
    end
  end
end
