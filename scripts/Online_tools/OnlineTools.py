import sys
import os
from mechanize import Browser

'''
Given a file with a list of permissions, finds the methods associated with each of those permissions. 

@input fileName - file with a list of permissions separated by a new line
@output prints out the dictionary, redirect standard output to a file
'''

def main(args):
    
    site = args[0]
    #permissions = args[0]
    for root, dirs, files in os.walk("/work/apps"):
        for f in files:
            

            filename = "/work/apps/" + f
            url = ""
            if(site == "comdroid"):
                url = "http://www.comdroid.org/"
            elif(site == "stowaway"):
                url = "http://www.android-permissions.org/"
            else:
                print "Unknown tool"
            print filename
            br = Browser()
            br.open(url)
            br.select_form(nr=0)
    
            br.form.add_file(open(filename), 'text/plain', filename)
            response = br.submit()

            results = open(site + '/' + f + '.txt', 'w')
            results.write(response.read())

    
    
    
    
    

    
    #results.write(mapping)

if __name__ == '__main__':
    args = sys.argv[1:]
    main(args)
