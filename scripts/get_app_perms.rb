#!/usr/bin/env ruby

# GATHER THE APP PERMISSIONS!

require 'mechanize'
# require 'debugger' ; Debugger.start(post_mortem: true)

class PlayScraper
  def initialize
    @apps = []
    @agent = Mechanize.new
  end

  def handle_app app_link
    # Fetch the permissions for the app at the given url
    app = {}
    page = app_link.click
    # Class = doc-banner-title identifies the title
    # id parameter is the full name of the application
    # doc-permission-description and doc-permission-description-full can be
    # grabbed pairwise to indicate which permissions the app has.
    app[:title] = page.search('h1.doc-banner-title').first.content
    app[:name] = Hash[URI.decode_www_form(app_link.uri.query)]['id']

    perm_names = page.search('div.doc-permission-description').collect do |element|
      element.content
    end
    perm_descriptions = page.search('div.doc-permission-description-full').collect do |element|
      element.content
    end
    
    app[:permissions] = Hash[perm_names.zip(perm_descriptions)]
    return app
  end

  def scrape(query, free=false)
    # debugger
    url = URI("https://play.google.com/store/search")
    # Params: q=<query>, price=1 (if free), start=multiples of 48, num=48
    params = {q: query, num: 48, c: "apps"}
    params[:price] = 1 if free
    i = 0
    loop do
      # Iterate through all apps from google play corresponding to the query,
      # then call handle_app on each app.
      
      # Construct url with parameters.
      params[:start] = i * params[:num] 
      url.query = URI.encode_www_form(params)

      # Open url, call handle_app on each link to an app.
      page = @agent.get(url)
      page.links_with(class: "title").each do |link|
        @apps << handle_app(link)
      end
      i += 1
    end

  rescue Mechanize::ResponseCodeError => e
    $stderr.puts e
  ensure
    puts @apps.inspect
  
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
