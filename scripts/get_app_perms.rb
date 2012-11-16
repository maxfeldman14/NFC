#!/usr/bin/env ruby

# GATHER THE APP PERMISSIONS!

require 'mechanize'

class PlayScraper
  def initialize
    @apps = []
    @agent = Mechanize.new
  end

  def handle_app app_url
    # Fetch the permissions for the app at the given url
    
  end

  def scrape(query, free=false)
    url = URI("https://play.google.com/store/search")
    # Params: q=<query>, price=1 (if free), start=multiples of 48, num=48
    params = {q: query, num: 48}
    params[:price] = 1 if free
    catch :done do
      loop do |i|
        # Iterate through all apps from google play corresponding to the query,
        # then call handle_app on each app.
        
        # Construct url with parameters.
        params[:start] = i * params[:num] 
        url.query = URI.encode_www_form(params)

        # Open url, call handle_app on each link to an app.
        page = @agent.get(url)
        page.links_with(class: "title").each do |link|
          handle_app link
        end

      end
    end
  end

end

if __FILE__ == $0
  unless (1..2).include? ARGV.size
    puts "Usage: #{$0} <query> [free=false]"
    exit -1
  end
  scraper = PlayScraper.new
  scraper.scrape(ARGV[0], ARGV[1] || false)
end
