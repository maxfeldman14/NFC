import sys
import os


def main(args):
    
    specificPerms = ['CALL_PHONE', 'SEND_SMS', 'NFC'] #specific permissions we want to see
    
    fileName = args[0]
    f = open(fileName, 'r')
    ls = []
    curApp = ''
    apps = []
    permCount = {}
    numApps = 0.0
    '''
    for line in f:
        line = line.replace('\n', '').replace('\r','').replace('android.permission.','')
        if line[0:2] not in ['..', '/U']: #otherwise it's an application and we can ignore it
            
            if line in permCount:
                permCount[line] += 1
            else:
                permCount[line] = 1
    
            #if 'NFC' in line:
                #print '!!!!!!!!!!' + line
        else:
            numApps += 1.0

    for key in permCount.keys():
        percent = (permCount[key]/numApps)*100
        if key in specificPerms:
            print key + ":" + str(permCount[key]) + ' = ' + str(percent)[0:4] + '%'
        elif percent > 20:
            print key + ":" + str(permCount[key]) + ' = ' + str(percent)[0:4] + '%'
    print numApps
'''
    n = False
    f = open(fileName, 'r')
    appPerms = {}
    nonNFCapps = []
    numNFCApps = 0.0
    curApp = ''
    for line in f:
        line = line.replace('\n', '').replace('\r','').replace('android.permission.','')
        if line[0:2] in ['..', '/U']:
            curApp = line
            appPerms[curApp] = []
            numApps += 1

        else:
            appPerms[curApp] += [line]

    for key in appPerms.keys():
        nfc = False
        for perm in appPerms[key]:
            if 'NFC' in perm:
                nfc = True
        if nfc:
            numNFCApps += 1.0
            for perm in appPerms[key]:
                if perm in permCount:
                    permCount[perm] += 1
                else:
                    permCount[perm] = 1

        nfc = False
                    
    for key in permCount.keys():
        percent = (permCount[key]/numNFCApps)*100
        if key in specificPerms:
            print key + ":" + str(permCount[key]) + ' = ' + str(percent)[0:4] + '%'
        elif percent > 20:
            print key + ":" + str(permCount[key]) + ' = ' + str(percent)[0:4] + '%'


if __name__ == '__main__':
    args = sys.argv[1:]
    try: 
        main(args)
    except IOError:
        print 'IOError: ' + str(IOError)
