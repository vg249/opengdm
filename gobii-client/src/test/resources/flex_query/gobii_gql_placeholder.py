#!/usr/bin/python

import sys

#pass string "fail" to cause returnVal to be nonzero
def main():
    returnVal = 0

    #print(len(sys.argv))
    #print(sys.argv[1])
    if len(sys.argv) > 0 and str(sys.argv[1]) == "fail":
        print("condition")
        returnVal = 1
    sys.exit(returnVal)

main()