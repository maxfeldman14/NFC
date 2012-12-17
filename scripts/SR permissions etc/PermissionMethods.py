import sys
import os

'''
Given a file with a list of permissions, finds the methods associated with each of those permissions. 

@input fileName - file with a list of permissions separated by a new line
@output prints out the dictionary, redirect standard output to a file
'''

def main(args):

    mapping = {}

    permissions = args[0]
    permissions = open(permissions, 'r')
    
    results = open('results.txt', 'w')

    for permission in permissions:
        #print permission
        permission = permission.replace('\n', '')
        #print 'grep ' + permission.replace('\n', '') + ' APICalls.txt > temp.txt'
        os.system('grep ' + permission + ' APICalls.txt > temp.txt')
        perm = open('temp.txt')
        lsOfVals = []
        for line in perm:
            # relevant API call is line.split[0]
            splitBySpace = line.split(' ')
            val = splitBySpace[0].split('\t')[0]
            lsOfVals += [val]
        mapping[permission] = lsOfVals

    print mapping
    #results.write(mapping)

if __name__ == '__main__':
    args = sys.argv[1:]
    main(args)
