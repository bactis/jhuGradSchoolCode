#Filename:      disassemblesaurus.py
#Date:          9/18/2014
#Author:        Ben Actis
#Email:         ben.actis@gmail.com
#Twitter:       @Ben_RA
#
#Notes          disassemblesaurus is a x86 linear sweep dissasmbler.
#               It only supports a limited subset of opcodes
#
#------------------------------------------------------------------
import argparse         #for parsing arguments via commandline
import binascii

#FunctionName:          main
#Parameters:            args.input  = input file for dissassembler
#                       args.offset = if a different starting offset is needed. Default 0
#Returns:               n/a
#Notes:                 -parsers command line arguments
#                       -starts the dissassembleFile function
def main():
    
    #variables
    parser = argparse.ArgumentParser(description='Dissassemble All Da Things Help Menu')
    offset = 0
    inputFile = None
    
    #values for command line input
    parser.add_argument('input', help='The file to dissassemble')
    parser.add_argument('-o', '--offset', help='Enter Starting offset, default is 0')
    args = parser.parse_args()

    print "the file provided is: " + args.input
    
    #call start dissassembly function
    dissambleFile(args.input)
    
#---------------------------------------------------------------------------   
#FunctionName       disassembleFile
#Parameters:        fileName - the name of the file being dissassembled
#                   offset   - the offset, defaults to 0
#Returns:           not sure yet, next instruction?
#Notes:             - The current instruction is stored in a python dictionary
#                   - Tries to open the file, and reads in 1 byte. Calls isValid Function
#                   - If byte is invalid prints invalid instruction and continues
#                   - If byte is falvid calls the identifyOptCodeFunction
#---------------------------------------------------------------------------
def dissambleFile(fileName, offset=0):
    
    #for tracking location within binary file
    position = 0
    
    #dictionary containing the current instruction
    #Done:     Whether the instruction is complete or not. Boolean value. True means no more bytes for this instruction
    #Location: first byte in the instruction
    #Length:   length in base 10 of the instruction 1-16
    #byts:     the actual byte values of the instrction 31 c0 etc
    #OptCode   = the instruction opt code xor, mov, add etc
    #modRM     = ???
    #opEn      = Used to determine mod rm, values can be MR, RM, ...
    #immediate = True/False value
    instruction = {
        'Done': True,
        'Location': 0,
        'Length': 0,
        'Bytes': None,
        'OptCode': None,
        'modrm': None,
        'opEn': None,
        'sib': None,
        'displacement': None,
        'immediate': False,
        'instruction': None,
        'src': None,
        'dst': None,
        'iBytes': []
    }
    
    #default valuess
    instructionDefault = instruction

    #open the file and try to read in first byte. Call hexlify to fix python unicode issue
    f = open(fileName, "rb")
    try:
        byte = f.read(1)
        byte = binascii.hexlify(byte)
        
        #while file is not empty
        while byte != "":
            
            # Do stuff with byte.
            #print binascii.hexlify(byte)
            
            #check if byte is invalid opt code. if its not alert user and continue
            if (isValid(byte) == False):
                print str(hex(position))+":\t"+binascii.hexlify(byte) + "\t Invalid Instruction"       #make display function?
                
            #check if byte is valid opt code
            elif isValid(byte) == True:
                
                #blank out instruction dictionary
                instruction = identifyOptCode(byte, instruction)
                
                #set location of instruction
                instruction['Location'] = position
                
                #call identify optCode and return dictionary
                instruction = identifyOptCode(byte, instruction)
                #print instruction
                
                #check if instruction is done. If not keep processing it, by reading in another byte
                while instruction['Done'] == False:
                    #print "processing instruction...."
                    
                    #if the instruction is not an immediate, read in bytes to process modrm byte
                    if (instruction['immediate'] == False):
                        byte=f.read(1)
                        position = position + 1
                        byte = binascii.hexlify(byte)
                        instruction = indentifyModrm(byte, instruction)
                        #print "indentifyModrm function done"
                        
                    #if the instruction is an immediate, read in bytes
                    elif (instruction['immediate'] == True ):
                        
                        print 'Immdiate found'
                        #temp variables x for loop, a[]list for storing immediate values
                        x = instruction['Length'] - 1
                        a = []
                        
                        #read 4 bytes (5 -1)
                        while x > 0:
                            byte=f.read(1)
                            position = position + 1
                            byte = binascii.hexlify(byte)
                            a.append(byte)
                            x = x -1
                            instruction['Bytes'] = instruction['Bytes'] + " "+ byte
                            
                        #reverse due to fun endianness
                        
                        a.reverse()
                        a.insert(0, '0x')
                        
                        #store in src
                        instruction['src'] = ''.join(a)
                        #print "immediate processing done"
                        instruction['Done'] = True
                        
                    
                
    
                printInstruction(instruction)
                
                #set values back to default
                instruction = instructionDefault
                #print instruction
                #loop that continues to read until instruction is done processing
                
            
            #read next byte and increment postion
            tt = raw_input()
            #print "press any key to cont"
            byte=f.read(1)
            byte = binascii.hexlify(byte)
            position = position + 1
            
    finally:
        f.close()
    #check the file for errors
    
    #read in 1 bytes

#---------------------------------------------------------------------------
#FunctionName       isValid
#Parameters         string of first byte in format 00
#Returns            False = invalid reads next byte and informs user
#                   True ???
#Notes              - a lot of code reuse here, perhaps merge this with identifyOptCode
#                   - add immediates
#---------------------------------------------------------------------------
def isValid(byte):
    
    #fix byte since python 2.7 likes everything to be unicode strings.
    #byte = binascii.hexlify(byte)
    
    #for debugging
    #print "\t\t\tDEBUG"
    #print "FunctionName: isValid"
    #print "byte is: " + byte
    
    
    #dictionary containing key value pairs
    #key = hex bytes, uniqu
    #pair  value non unique   
    optCode = {
      
        # ADDs  
        '04': 'add', '05': 'add', '00': 'add',                  #ADD Immediate to a register    ?
        '01': 'add', '02': 'add', '03': 'add',                  #ADD r to r/m                   DONE
        
        # XORs
        '34': 'xor', '35': 'xor',                               #XOR eax's with an immediate    ?
        '80': 'xor', '81': 'xor', '83': 'xor',                  #XOR r/m with immediate         ?
        '30': 'xor', '31': 'xor',                               #XOR r/m to r                   DONE
        '32': 'xor', '33': 'xor',                               #XOR r  to  r/rm                ?
        
        #PUSH's
        '50': 'push', '51': 'push', '52': 'push', '53': 'push', #PUSH for registers             DONE
        '54': 'push', '55': 'push', '56': 'push', '57': 'push', #       ... cont
        # put push r/m here
        # put push immediate here
        # put push little baby registers here
        
        #MOV's
        '88': 'mov', '89': 'mov',                               #mov r to r/m                   DONE
        '8a': 'mov', '8b': 'mov',                               #mov r/m to r                   NOT DONE
        
        '8c': 'mov', '8e': 'mov',                               #double check if i have to handle these?
        
        'a0': 'mov', 'a1': 'mov', 'a2': 'mov','a3': 'mov',      #mov some offset thing          ???
        'a4': 'mov', 'a5': 'mov', 'a6': 'mov','a8': 'mov',      #       ... cont
        
        'b0': 'mov', 'b1': 'mov', 'b3': 'mov','b4': 'mov',      #mov immediate i8 to r8         ???
        #add more here to continue series
        
        'b8': 'mov', 'b9': 'mov', 'ba': 'mov','bb': 'mov',      #mov immediate to r             DONE
        'bc': 'mov', 'bd': 'mov', 'be': 'mov','bf': 'mov',      #       ... cont
        
    }

    #iterate through all the opt codes
    for key in optCode:
        
        #if the byte is equal to the key set it too...
        if byte == key:
            return True
            
        
    return False

#---------------------------------------------------------------------------
#FunctionName       identifyOptCode
#paramaters         byte = the current byte being examined
#returns
#notes              There's code resource here. Perhaps combine this is is
#                   valid opt code function
#
#                   what works
#                   xor: registers
#                   add: registers
#                   push: registers
#                   mov: registers
#
#                   whats left
#                   how to handle immediates?
#                   how to handle dwords?
#---------------------------------------------------------------------------
def identifyOptCode(byte, instruction):
    
    #print type(instruction)
    #print instruction
    #print instruction.keys()
    #print instruction[OptCode]
    
    #Questions to ask prof?
    # how to handle add 80 /0 ib
    # how to handle rex + 80 /0 ib
    # how to handle 81 /0 iw
    # how to ahndle 81 /0 id
    #how to handle 83 /0 ib
    # how to handle immediates with mov
    
    #print "byte is: " + byte
    
    #keys = 34, 35 etc
    #values = mnemonics
    optCode = {
      
        # ADDs  
        '04': 'add', '05': 'add', '00': 'add',                  #ADD Immediate to a register    ?
        '01': 'add', '02': 'add', '03': 'add',                  #ADD r to r/m                   DONE
        
        # XORs
        '34': 'xor', '35': 'xor',                               #XOR eax's with an immediate    ?
        '80': 'xor', '81': 'xor', '83': 'xor',                  #XOR r/m with immediate         ?
        '30': 'xor', '31': 'xor',                               #XOR r/m to r                   DONE
        '32': 'xor', '33': 'xor',                               #XOR r  to  r/rm                ?
        
        #PUSH's
        '50': 'push', '51': 'push', '52': 'push', '53': 'push', #PUSH for registers             DONE
        '54': 'push', '55': 'push', '56': 'push', '57': 'push', #       ... cont
        # put push r/m here
        # put push immediate here
        # put push little baby registers here
        
        #MOV's
        '88': 'mov', '89': 'mov',                               #mov r to r/m                   DONE
        '8a': 'mov', '8b': 'mov',                               #mov r/m to r                   NOT DONE
        
        '8c': 'mov', '8e': 'mov',                               #double check if i have to handle these?
        
        'a0': 'mov', 'a1': 'mov', 'a2': 'mov','a3': 'mov',      #mov some offset thing          ???
        'a4': 'mov', 'a5': 'mov', 'a6': 'mov','a8': 'mov',      #       ... cont
        
        'b0': 'mov', 'b1': 'mov', 'b3': 'mov','b4': 'mov',      #mov immediate i8 to r8         ???
        #add more here to continue series
        
        'b8': 'mov', 'b9': 'mov', 'ba': 'mov','bb': 'mov',      #mov immediate to r             DONE
        'bc': 'mov', 'bd': 'mov', 'be': 'mov','bf': 'mov',      #       ... cont
    }
    
    #iterate through all the opt codes
    for key in optCode:
        #print "key is: " + key + " key value is: " + optCode[key]      #debuging
        
        #if the byte is equal to the key set modify appropriate field in instruction dictionary
        if byte == key:
            
            #keep track of the bytes for the instruction, add the current byte to it
            instruction['Bytes'] = byte
            
            #set the instruction to the appropriate value (xor add etc)
            temp = optCode[key]
            instruction['OptCode'] = temp
            
            #set appropriate opEn mode here    XOR specific
            if (key == '30' or key == '31'):
                instruction['opEn'] = 'MR'
                #print "opEn value set"
                #print "changed done to False"
                instruction['Done'] = False
            
            #ADD     REG      REG
            #set appropriate opEn mode here ADD specific    
            if (key == '01' or key == '02' or key == '03'):
                instruction['opEn'] = 'MR'
               # print "opEn value set"
                #print "changed done to False"
                instruction['Done'] = False
            
            #PUSH      REG    
            #for push calculate register being modified
            if (instruction['OptCode'] == 'push'):
                #call calculate push
                instruction = calculatePush(byte, instruction)
            
            #MOV    REG    REG    
            #for mov register, set done to false so modrm function runs next
            if (key == '88' or key == '89'):
                instruction['opEn'] = 'MR'
                instruction['Done'] = False
                
            # MOV    R/M     R set down to false so modrm function runs next
            if (key == '8b' or key == '8a'):
                instruction['opEn'] = 'RM'
                instruction['Done'] = False
            
            # MOV    REG     IMEDIATE VALUE    
            #for mov memory value. Remember its optcode (b8 + value of register)
            if (key == 'b8' or key == 'b9' or key == 'ba' or key == 'bb'):
                instruction['Done'] = False
                instruction['immediate'] = True
                
                #set appropriate dst register
                if(key == 'b8'):
                    instruction['dst'] = 'eax'
                    
                elif(key == 'b9'):
                    instruction['dst'] = 'ecx'
                    
                elif(key == 'ba'):
                    instruction['dst'] = 'edx'
                    
                elif(key == 'bb'):
                    instruction['dst'] = 'ebx'
                    
                elif(key == 'bc'):
                    instruction['dst'] = 'esp'
                
                elif(key == 'bd'):
                    instruction['dst'] = 'ebp'
                
                elif(key == 'be'):
                    instruction['dst'] = 'esi'
                
                elif(key =='bf'):
                    instruction['dst'] = 'edi'
                
                #set length to 5 (so it reads in 5 -1 bytes)
                instruction['Length'] = 5

            #do some other checks here and possibly set instruction['done'] to True
            
            
            return instruction 


#---------------------------------------------------------------------------
#FunctionName       identifyModrm
#paramaters         instruction = dict of current instruction values
#returns            n/a
#notes              -takes the byte and converts to bits (string)
#                   -breaks up the bits to specific values: modBits, dstBits, srcBits

#---------------------------------------------------------------------------
def indentifyModrm(byte, instruction):

    #print "identifyModrm function running"


    #add byte to the instruction
    instruction['Bytes'] = instruction['Bytes'] + " " + byte
    #debug
    #print "byte: " + byte,
    #print "type: ",
    #print type(byte)
    
    #print "converted value is",
    #print bin(binascii.unhexlify(byte))
    
    #get byte value to bits
    bits = int(byte, 16)
    bits = bin(bits)
    
    
    #print bits  #for debug
    
    #check for size of bits
    if len(bits) < 8:
        print "ERROR: NOT ENOUGH BITS!"
    
    #strip off the 0b and store rest in a list
    bits = bits[2:]
    
    #determine mod, remove those bits from string bits
    modBits = bits[:2]
    bits = bits[2:]
    #print "modbits: " + modBits
    
    #regbits
    regBits = bits[:3]
    bits = bits[3:]
    #print "reg bits: " + regBits
    
    #2nd operand src
    rmBits = bits[:3]
    #print "rmbits: " + rmBits
    
    #print modBits + " " + regBits + " " + rmBits + " "
    
    #MR = 1st operand goes in r/m and 2nd goes in reg
    if instruction['opEn'] == 'MR':
        dstBits = rmBits
        srcBits = regBits
        
        #set mneumoics of src and dest
        instruction['src'] = bitsToRegister(srcBits)
        instruction['dst'] = bitsToRegister(dstBits)
        
    
    #set instruction done to true?
    instruction['Done'] = True
    #tt = raw_input()
    #print "press a key"
    
    return instruction
    
    #print "press any key to cont"

#---------------------------------------------------------------------------
#FunctionName       printInstruction
#paramaters         instruction = dict of current instruction values
#returns            n/a
#notes
#---------------------------------------------------------------------------
def printInstruction(instruction):

        print str(hex(instruction['Location'])) + ":\t",
        print instruction['Bytes'] + "\t\t",
        print instruction['OptCode'],
        
        if instruction['dst'] != None:
            print instruction['dst'],
            
        if instruction['src'] !=None:
            print instruction['src'],
            
        print ""


#---------------------------------------------------------------------------
#FunctionName       bitsToRegister
#paramaters         bits = 3 bits that relate to what register
#returns            string of a register (eax, ecx etc)
#notes
#---------------------------------------------------------------------------
def bitsToRegister(bits):
    
    reg = {
      
        '000': 'eax',
        '001': 'ecx',
        '010': 'edx',
        '011': 'ebx',
        '100': 'esp',
        '101': 'ebp',
        '110': 'esi',
        '111': 'edi',
    }
    
    #iterate through all the opt codes
    for key in reg:
        
        #debug line
        #print "key is: " + key + " bits we want are: " + bits
        
        if bits == key:
            return reg[key]
            
    return "ERROR"
    

#---------------------------------------------------------------------------
#FunctionName:      calcuatePush
#Parameters:        instruction - dictionary containing attributes of current instructor
#Returns:           instruction - dictionary containing attributes of current instruction
#                       (with updated done and src, and dsts)
#Notes:             -Currently handles only push register onto stack
#                   - add memory values?
#                   - add immediates?
def calculatePush(byte, instruction):
    
    if (byte == '50'):
        instruction['dst'] = 'eax'
        
    elif (byte == '51'):
        instruction['dst'] = 'ecx'
        
    elif (byte == '52'):
        instruction['dst'] = 'edx'
    
    elif (byte == '53'):
        instruction['dst'] = 'ebx'
    
    elif (byte == '54'):
        instruction['dst'] = 'esp'
        
    elif(byte == '55'): 
        instruction['dst'] = 'ebp'
    
    elif(byte == '56'):
        instrcution['dst'] = 'esi'
        
    elif (byte == '57'):
        instruction['dst'] = 'edi'
        
    else:
        instruction['dst'] = 'ERROR'
    
    #so code moves to next instruction
    instruction['Done'] = True
    instruction['src'] = ''
    
    return instruction
main();
