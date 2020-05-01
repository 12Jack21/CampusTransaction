import os
import sys
from optparse import OptionParser

parser = OptionParser()
parser.add_option('--project', '-p', type= str,default='b',help='project flag, (b)ackend,(f)rontend or (d)ocs')

def main():
    options,args = parser.parse_args()
    print("options: ", options)
    print("args: ", args)


    prefix_dir = './'
    file = '.gitignore'
    if not os.path.exists(prefix_dir + file):
        print(".gitignore file doesn't exists.")
        quit()
    prefix_dict = {'b':'Backend','f':'Frontend','d':'docs'}
    with open(prefix_dir + file,'r') as f:
        items = f.read().split('\n')
        for i in range(len(items)):
            item = items[i]
            p = ''
            if len(item) == 0 or item.startswith('#') or item == '.git/':
                continue
            if item.startswith('!'):
                item = item[1:]
                p = '!'
            skip = False
            for key in prefix_dict.keys():    
                if item.startswith(prefix_dict[key]):
                    skip = True
                    break
                if options.project == key:
                    if item.startswith('/'):
                        item = prefix_dict[key] + item
                    else:
                        item = prefix_dict[key] + '/' + item
                    break

            if not skip: items[i] = p + item

    with open(prefix_dir + file, 'w') as f:
        print("Writing to new .gitignore :")
        for item in items:
            f.write(item + '\n')
            print(item)


if __name__ == '__main__':
    main()