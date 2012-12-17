import sys
import os
from mechanize import Browser

'''
Given a file with a list of permissions, finds the methods associated with each of those permissions. 

@input fileName - file with a list of permissions separated by a new line
@output prints out the dictionary, redirect standard output to a file
'''

def main(args):
    url = "https://play.google.com/store/search?q=nfc&c=apps"
    br = Browser()
    br.open(url)
    #permissions = args[0]
    while(true):
        
        br.select_form
            br = Browser()
            br.open(url)
            br.select_form(nr=0)
    
            
            response = br.submit()

            results = open(site + '/' + f + '.txt', 'w')
            results.write(response.read())

    
    
    
    
    

    
    #results.write(mapping)

if __name__ == '__main__':
    args = sys.argv[1:]
    main(args)
