import sys
import os

def main(args):
    dir = args[0]
    for app in os.listdir(dir):
        app = app.replace('.', '')
        os.system('mkdir ' + app)
        print app
        
        path = '~/Desktop/classes/CS261/project/temp/' 
        appPath = path + app
        os.chdir(app)
        os.system('ls')
        
        command = 'ruby ../jadder.rb ../../classes/' + app + ' ~/Desktop/classes/CS261/project/temp/jad'
                  
        print command
        os.system(command)

        os.system(path)




if __name__ == '__main__':
    args = sys.argv[1:]
    try:
        main(args)
    except IOError:
        print 'IOError: ' + str(IOError)
