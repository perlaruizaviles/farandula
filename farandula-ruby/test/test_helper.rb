$LOAD_PATH.unshift File.expand_path('../lib/**/*', __FILE__)

require 'farandula'

require 'minitest/autorun'
require 'minitest/unit'
require 'minitest/pride'
require "minitest/reporters"


Minitest::Reporters.use! Minitest::Reporters::SpecReporter.new

