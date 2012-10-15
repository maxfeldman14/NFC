#!/usr/bin/env ruby
require 'find'

# Go through all the files of all directories and jad .class files

def jadder dir
  Find.find(dir) do |path|
    # Check if file
    if not FileTest.directory? path
      # Check if class
      if File.basename(path.downcase).split(".").last == "class"
        # JAD it
        `./jad #{path}`
      end
    end
  end
end


if __FILE__ == $0
  jadder ARGV[0]
end 
