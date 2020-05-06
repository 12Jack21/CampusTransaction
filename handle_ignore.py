import os
import sys
from optparse import OptionParser

parser = OptionParser()
parser.add_option('--project', '-p', type= str,default='b',help='project flag, (b)ackend or (f)rontend')

def main():
    options,args = parser.parse_args()
    print("options: ", options)
    print("args: ", args)


    prefix_dir = './'
    file = '.gitignore'
    if not os.path.exists(prefix_dir + file):
        print(".gitignore file doesn't exists.")
        quit()

    with open(prefix_dir + file,'r') as f:
        items = f.read().split('\n')
        for i in range(len(items)):
            item = items[i]
            p = ''
            if len(item) == 0 or item.startswith('#'):
                continue
            if item.startswith('!'):
                item = item[1:]
                p = '!'
            if options.project == 'b':
                if item.startswith('Backend') continue
                if item.startswith('/'):
                    item = 'Backend' + item
                else:
                    item = 'Backend/' + item
            elif project == 'f':
                if item.startswith('Frontend') continue
                if item.startswith('/'):
                    item = 'Frontend' + item
                else:
                    item = 'Frontend/' + item
            items[i] = p + item

    with open(prefix_dir + file, 'w') as f:
        print("Writing to new .gitignore :")
        for item in items:
            f.write(item + '\n')
            print(item)


if __name__ == '__main__':
    main()