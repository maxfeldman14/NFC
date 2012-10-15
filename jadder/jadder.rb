#!/usr/bin/env ruby
require 'find'

# Go through all the files of all directories and jad .class files
# TODO: operate as if jad was called from within the same dir as the .class
# file, in order to have .jad and .class in the same place

def jadder(dir, jad_loc)
  seen = {}
  Find.find(dir) do |path|
    # Check if file
    if not FileTest.directory? path
      # Check if class
      if File.basename(path.downcase).split(".").last == "class"
        # JAD it
        `#{jad_loc} -o #{path}`
      end
    end
  end
end


if __FILE__ == $0
  # The location of the directory to be processed and the jad binary
  jadder ARGV[0], ARGV[1]
end 
