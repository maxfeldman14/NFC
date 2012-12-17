import sys
import os


def main(args):
    dir = args[0]
    for apkFile in os.listdir(dir):
        #print apkFile
        if str(apkFile) != '.DS_Store':
            
            newFile = str(dir) + '/' + str(apkFile.replace(' ', ''))
            command = './analyze-apk ' + newFile
            #print command
            mvcommand = 'mv ' + str(dir) + '/' + apkFile.replace(' ', '\ ') + ' ' + newFile
            
            os.system(mvcommand)
            os.system(command)
    

if __name__ == '__main__':
    args = sys.argv[1:]
    try: 
        main(args)
    except IOError:
        print 'IOError: ' + str(IOError)
