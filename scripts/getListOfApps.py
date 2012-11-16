import sys
import os
from mechanize import Browser
from bs4 import BeautifulSoup


def main(args):
    page = 20
    applications = []
    while(True):
        try:
            url = "https://play.google.com/store/search?q=nfc&c=apps&start=" + str(page*24) + "&num=24"
            print url
        
            br = Browser()
            br.set_handle_robots(False)
            response = br.open(url)
        
            soup = BeautifulSoup(response.read())
            #print soup.prettify()
            #soup.prettify()
        
            liList = soup.findAll('li')
            for item in liList:
                typ = item['class']
                #print typ
                if(typ == [u'search-results-item']):
                    #print item
                    tempList = item.findAll('a', 'title')
                    for temp in tempList:
                        try:
                            temp = str(temp['title'])
                            applications += [temp]
                        except Exception:
                            continue
                        
            page += 1
        except Exception as e:
            print e
            continue
    print applications
                        
                #for child in item.children:
                #    print child
        
        

if __name__ == '__main__':
    args = sys.argv[1:]
    main(args)
