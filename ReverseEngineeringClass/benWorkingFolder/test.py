#!/usr/bin/python
import binascii

fileName = "example1.o"

with open(fileName, "rb") as f:
    #while file is not empty
    try:
        while 1:
            # Do stuff with byte.
            temp = f.read(1)
            if temp:
                #print hex(ord(temp))
                print binascii.hexlify(temp)
                print "press any key to cont"
                tt = raw_input()
            else:
                break

    except Exception as e:
        print e
