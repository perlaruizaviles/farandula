require 'test_helper'
require 'file_helper'
require 'string_helper'
require 'minitest/autorun'
require 'farandula/utils/logger_utils'

module Farandula
  class LoggerUtilsTest < Minitest::Test
    include Farandula::Utils
    def test_logger_pretty_json
      ugly_json = '{"b":42.005,"a":[42,17],"longer":true,"str":"yes please"}'
      expected = '{' + "\n" \
                  '  "b": 42.005,' + "\n" \
                  '  "a": [' + "\n" \
                  '    42,' + "\n" \
                  '    17' + "\n" \
                  '  ],' + "\n" \
                  '  "longer": true,' + "\n" \
                  '  "str": "yes please"' + "\n" \
                  '}'

      actual = LoggerUtils.get_pretty_json(ugly_json)
      assert_equal(
        expected,
        actual
      )
    end
    
    def test_logger_pretty_xml
      ugly_xml = '<?xml version="1.0" encoding="UTF-8"?><note><to>Nearsoft.inc</to><from>Quantums-Team</from>' \
        '<heading>Greetings</heading>' \
        '<body>Hello, nearsoft!</body></note>'

      expected =  '<?xml version="1.0" encoding="UTF-8"?>' + "\n" \
                  '<note>' + "\n" \
                  '  <to>Nearsoft.inc</to>' + "\n" \
                  '  <from>Quantums-Team</from>' + "\n" \
                  '  <heading>Greetings</heading>' + "\n" \
                  '  <body>Hello, nearsoft!</body>' + "\n" \
                  '</note>' + "\n"

      actual = LoggerUtils.get_pretty_xml(ugly_xml)

      assert_equal(
        expected,
        actual
      )
    end
  end
end
