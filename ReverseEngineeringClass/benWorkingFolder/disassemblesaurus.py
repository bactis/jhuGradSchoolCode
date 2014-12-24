#Filename:      disassemblesaurus.py
#Date:          9/18/2014
#Author:        Ben Actis
#Email:         ben.actis@gmail.com
#Twitter:       @Ben_RA
#
#Notes          disassemble saurus is a x86 linear sweep dissasmbler.
#               It only supports a limited subset of opcodes
#               example1.o works
#               example2.o works
#               ex2   partially working. Got too late needed to hand in
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
    parser = argparse.ArgumentParser(description='Disassemblesaurus Help Menu')
    offset = 0
    inputFile = None
    
    #values for command line input
    parser.add_argument('input', help='The file to disassemble')
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
    instruction = {
        'Valid': False,         #     Whether instructino is invalid or not, if invalid skip and keep going
        'Done': True,           #     Whether the instruction is complete or not. Boolean value. 
                                #        True means no more bytes for this instruction
        'Location': 0,          #     Where in the binary file it is found.  1st column of output
        'Length': 0,            #     Size of instruction base 10 of the instruction 1-16
        'Bytes': None,          #     String for bytes. 2nd column of output 31 c0 etc
        'OptCode': '?',         #     3rd column of output the add, xor, mov, push, pop etc
        'modrm': None,          #     set to false to skip modRM function(for example with ret instruction)
        'opEn': None,           #     operator encoding Used to determine mod rm, values can be MR, RM, ...
        'sib': None,            #     ?
        'displacement': None,   #     ?
        'immediate': False,     #     Boolean value if an immediate is used
        'instruction': None,    #     ?
        'src': "",              #     source operand
        'dst': "",              #     destination operand
        'iBytes': [],           #     ?
        'dword': False,         #     boolean value if a dword is used for dst reg
        'byte': False,          #     boolean value if a byte is used for dst reg
        'mod': None,            #     for easier printing of instructions 00,01,10,11
        'complexOp': False,     #      for complex opt codes for example 80, 81 etc opEn mode is usually MI for these
    }
    
    #default valuess
    instructionDefault = instruction

    #open the file and try to read in first byte. Call hexlify to fix python unicode issue
    f = open(fileName, "rb")
    try:
        byte = f.read(1)
        byte = binascii.hexlify(byte)
        position = 0
        
        
        #while file is not empty
        while byte != "":
                
            #blank out instruction dictionary set to default values?
            #instruction.update(instructionDefault)
                
            #set location of instruction
            instruction['Location'] = position
                
            #call identify optCode and return dictionary
            #   -can set opEn to MI which will effect logic below
            instruction = identifyOptCode(byte, instruction)
            
            #if invalid print out instruction now
            if instruction['Valid'] == False:
                #tt=raw_input()
                #handle 2 byte instructions not supported
                if byte =='ff':
                    byte=f.read(1)
                    position = position + 1
                    byte = binascii.hexlify(byte)
                    instruction['Bytes'] = instruction['Bytes'] + " " + byte
                    printInstruction(instruction)
                else:
                    printInstruction(instruction)
                
            #If instruction is valid
            elif instruction['Valid'] == True:
                
                    
                #check if instruction is done. If not keep processing it, by reading in another byte
                while instruction['Done'] == False:
                   
                    #begin proccessing the modr/m byte. can return several results
                    #  - can set immediate to true meaning the next bytes are an immediate value
                    #  - can set dword to to true, meaning next byte is a dword
                    #  - will set mod which is the 00,01,10,11 for easier printing
                    if (instruction['immediate'] == False and instruction['modrm']!= False):
                        byte=f.read(1)
                        position = position + 1
                        byte = binascii.hexlify(byte)
                        instruction = indentifyModrm(byte, instruction)
                     
                     
                     
                        
                    #if the instruction is an immediate, read in bytes for the immediate value
                    # NOTES!!!!!!!!!!!!!
                    #    prob should have this read in a stored value of bytes left to read in
                    if (instruction['immediate'] == True ):
                        
                        #print "immediate condition met?"
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
                        
                        if instruction['opEn'] != 'MI':
                            
                            #store in src
                            instruction['src'] = ''.join(a)
                            instruction['Done'] = True
                        
                        #inverse for this one   
                        elif instruction['opEn'] == 'MI':
                            instruction['dst'] = ''.join(a)
                            instruction['Done'] = True
                            
                        else:
                            instruction['Done'] = True
                    
                    #if the instruction uses a dword
                    if(instruction['dword'] == True):
                        byte=f.read(1)
                        position = position + 1
                        byte = binascii.hexlify(byte)
                        instruction = indentifyModrm(byte, instruction)
                        instruction['src'] = instruction['src'] + byte + " ]"
                        instruction['Done'] = True
                
                
                    #if the instruction uses a byte for dest based on mod bits of mod/rm
                    #the next byte read in is the dest reg + byte
                    if(instruction['byte'] == True):                        
                        byte=f.read(1)
                        position = position + 1
                        byte = binascii.hexlify(byte)
                        instruction['src'] = instruction['src'] + " + "+ byte + " ]"
                        instruction['Done'] = True
                        
                
                    #for handling ret's where 2 bytes will need to be read in
                    if(instruction['OptCode'] == 'ret' and instruction['Done'] == False and instruction['modrm'] == False):
                        for x in range(0,2):
                            byte=f.read(1)
                            position = position + 1
                            byte = binascii.hexlify(byte)
                            if x != 0:
                                instruction['src'] = byte +  " " + instruction['src']
                            else:
                                instruction['src'] = byte

                            instruction['Bytes'] = instruction['Bytes'] + " "+ byte
                            
                        instruction['Done'] = True #end this condition
                     
                    
                    #for handling 1 byte jcc
                    if(instruction['OptCode'] == 'jcc' and instruction['Done'] == False and instruction['modrm'] == False):
                         byte=f.read(1)
                         eip = position      #for calculating jcc
                         position = position + 1
                         byte = binascii.hexlify(byte)
                         instruction['Bytes'] = instruction['Bytes'] + " "+ byte
                         byte = '0x'+ byte 
                         byte = eval(byte)
                         #calculate label       #eip is hex already,   
                         instruction['src'] = hex(eip + 0x2 + byte)
                         instruction['Done'] = True
                         
                    #for handling e8 call where next 4 bytes are for label
                    if(instruction['Bytes'] == 'e8'):
                        eip = position      #for calculating call
                        
                        for x in range(0,4):
                            byte=f.read(1)
                            position = position + 1
                            byte = binascii.hexlify(byte)
                            
                            #debug menu oh joy
                            #print "eip: " + str(eip)
                            
                            instruction['Bytes'] = instruction['Bytes'] + " "+ byte
                            instruction['src'] = hex(eip + 0x4 + 0x1) #add current location + 4 byts + size of instruction
                            instruction['Done'] = True
                            
                            
                            
                printInstruction(instruction)
                
                
            #set values back to default
            instruction.update(instructionDefault)
            #print "DEBUG-------------- VALUES RESET"
            instruction = {
                'Valid': False,         #     Whether instructino is invalid or not, if invalid skip and keep going
                'Done': True,           #     Whether the instruction is complete or not. Boolean value. 
                                        #     True means no more bytes for this instruction
                'Location': 0,          #     Where in the binary file it is found.  1st column of output
                'Length': 0,            #     Size of instruction
                'Bytes': '  ',          #     String for bytes. 2nd column of output
                'OptCode': '?',         #     3rd column of output the add, xor, mov, push, pop etc
                'modrm': False,         #     ? set to false to skip
                'opEn': '',             #     operator encoding
                'sib': None,            #     ?
                'displacement': None,   #     ?
                'immediate': False,     #     Boolean value if an immediate is used
                'instruction': None,    #     ?
                'src': "",              #     source operand
                'dst': "",              #     destination operand
                'iBytes': [],           #     ?
                'dword': False,         #     boolean value if a dword is used for dst reg
                'byte': False,          #     boolean value if a byte is used for dst reg
                'mod': None,             #     for easier printing of instructions 00,01,10,11
                'complexOp': False,     #      for complex opt codes for example 80, 81 etc opEn mode is usually MI for these
                
            }
            #print instruction
            #tt=raw_input()
                
            #----DEBUG CODE REMOVE LATER ---_#
            #read next byte and increment postion
            #tt = raw_input()
            #print "press any key to cont"
            byte=f.read(1)
            byte = binascii.hexlify(byte)
            position = position + 1
            
    finally:
        f.close()
    #check the file for errors
    
    #read in 1 bytes


#---------------------------------------------------------------------------
#FunctionName       identifyOptCode
#paramaters  a       byte = the current byte being examined
#returns
#notes              There's code resource here. Perhaps combine this is is
#                   valid opt code function
#
#                   Left to finish
#                   ADD, AND, BSWAP, CALL, CMP, 
#                   DEC, IDIV, iMUL, MUL, NEG
#                   NOP, NOC, INC, JMP, LEA
#                   MOV, OR, PUSH, POP, 
#                   SAL, SAR, SBB, SHL, SHR, 
#                   TEST, XOR
#
#                   DONE / READY TO TEST
#                   RET
#
#
#---------------------------------------------------------------------------
def identifyOptCode(byte, instruction):
        
    #keys = 34, 35 etc
    #values = mnemonics
    optCode = {
      
        # ADDs  ------------------------------------------------------------------------------------------
        '04': 'add', '05': 'add',           #ADD    Immediate to AL, AX, EAX        NOT DONE
        '80': '???', '81': '???',           #ADD    immediate to r/m                NOT DONE        (2 byte op)
#        '83': '???',                        #ADD    sign extended immediate to r/m  NOTE DONE       (2 byte opt)
        '00': 'add', '01': 'add',           #ADD    r/m to r        MR              READY TO TEST                  
        '02': 'add', '03': 'add',           #ADD    r to r/m                        DONE
        
        # AND   ------------------------------------------------------------------------------------------                      
        '24': 'and', '25': 'and',           #AND    immediate to AL,AX, EAX         NOT DONE 
        '80': '???', '81': '???',           #AND    r/m and immediate               NOT DONE    (2 byte op)
        '83': '???',                        #AND    r/m and signed immedaite        NOT Done    (2 byte op)
        
        '20': 'and', '21': 'and',           #AND    r/m and r                       READY TO TEST
        '22': 'and', '23': 'and',           #and    r and r/m                       READY TO TEST
        
        #BSWAP ------------------------------------------------------------------------------------------
        '0f': 'bswap',                      #BSWAP                                  NOT DONE    how to handle mode O?
        
        #CALL ------------------------------------------------------------------------------------------
        'e8': 'call',                       #CALL near relative displacement        DONE I THINK
        'ff': '???',                        #CALL near absolute address given r/m   NOT DONE
        '9a': 'call',                       #CALL far absolute address in operand   NOT DONE
        'ff': '???',                        #CALL far absolute absoulte in m        NOT DONE
        
        #CMP ------------------------------------------------------------------------------------------
        '3c': 'cmp', '3d': 'cmp',           #CMP im with al ea eax                   NOT DONE
        '80': '???', '81': '???',           #CMP im with r/m                         NOT DONE   (2 byte op)
        '83': '???',                        #CMP sign extended immediate to r/m      NOTE DONE  (2 byte opt)
        '38': 'cmp', '39': 'cmp',           #CMP r with r/m     MR                   READY TO TEST
        '3a': 'cmp', '3b': 'cmp',           #CMP r/m with r     RM                   READY TO TEST 
        
        #DEC ------------------------------------------------------------------------------------------
        'fe': '???', 'ff': '???',           #DEC r/m by 1                          NOT DONE
        '48': 'dec',                        #DEC r by 1                            NOT DONE
        
        #IDIV ------------------------------------------------------------------------------------------
        'f6': '???', 'f7': '???',           #IDIV                                  NOT DONE
        
        #IMUL------------------------------------------------------------------------------------------
        'f6': '???', 'f7': '???',                               #IMUL                 NOT DONE
        '0f': 'imul',                                           #IMUL                 NOT DONE *ask prof
        '6b': 'imul', '69': 'imul',                              #IMUL                 NOT DONE *ask prof
        
        #INC------------------------------------------------------------------------------------------
        'fe': '???', 'ff': '???',                               #INC                  NOT DONE
        '40': 'inc',                                            #INC                  NOT DONE
        
        #JMP------------------------------------------------------------------------------------------
        'eb': 'jmp', 'e9': 'jmp',                               #JMP    NOT DONE
        'ff': '???', 'ea': 'jmp',                               #JMP    NOT DONE      * ask prof?
        #                                                       ask about JNZ??????
        
        #other jumps here for 8 bit displacement
        '70': 'jcc', '71': 'jcc',                               # I think?
        '72': 'jcc', '73': 'jcc', '74': 'jcc', '75': 'jcc',     # I think?   74 is in testing  
        '76': 'jcc', '77': 'jcc', '78': 'jcc', '79': 'jcc',     # I think?   *ask prof how to hangle all of these
        '7b': 'jcc', '7c': 'jcc', '7d': 'jcc',                  # I think?
        '7e': 'jcc', '7f': 'jcc',                               # I think?
        'e3': 'jcc',                                            # I think?
        
        #LEA------------------------------------------------------------------------------------------
        '8d': 'lea',                                            #LEA    DONE
        
        #MUL------------------------------------------------------------------------------------------
        'f6': '???', 'f7': '???',                               #MUL    NOT DONE *ASK PROF???
        
        
        #MOV's  -------------------------------------------------
        '88': 'mov', '89': 'mov',                               #mov r to r/m                   DONE
        '8a': 'mov', '8b': 'mov',                               #mov r/m to r                   DONE
        
        '8c': 'mov', '8e': 'mov',                               #double check if i have to handle these?
        
        'a0': 'mov', 'a1': 'mov', 'a2': 'mov','a3': 'mov',      #mov some offset thing          ???
        'a4': 'mov', 'a5': 'mov', 'a6': 'mov','a8': 'mov',      #       ... cont
        
        'b0': 'mov', 'b1': 'mov', 'b3': 'mov','b4': 'mov',      #mov immediate i8 to r8         ???
        #add more here to continue series
        
        'b8': 'mov', 'b9': 'mov', 'ba': 'mov','bb': 'mov',      #mov immediate to r             DONE
        'bc': 'mov', 'bd': 'mov', 'be': 'mov','bf': 'mov',      #       ... cont
        
        #NEG------------------------------------------------------------------------------------------
        'f6': '???', 'f7': '???',                               #NEG    NOT DONE *ask prof
        
        #NOP------------------------------------------------------------------------------------------
        '90': 'nop', '0f': 'nop',                               #NOP  NOT DONE
        
        #NOT------------------------------------------------------------------------------------------
        'f6': '???', 'f7': '???',                               #NOT    NOT DONE *ask prof?
        
        #OR
        '0c': 'or', '0d': 'or',                                 #OR     NOT DONE
        '80': '???','81': '???', '83': '???',                   #OR     NOT DONE
        '08': 'or', '09': 'or',                                 #OR     NOT DONE  check for duplicates?
        '0a': 'or', '0b': 'or',                                 #OR     NOT DONE  check for duplicates?
        
        
        
        #PUSH's ---------------------------------------------
        'ff': '???',   # 2byte opt codes here                 #                                NOT DONE r/m32
        '50': 'push', '51': 'push', '52': 'push', '53': 'push', #PUSH for registers             DONE
        '54': 'push', '55': 'push', '56': 'push', '57': 'push', #       ... cont
        '6a': 'push', '6b': 'push',                             # NOT DONE      for immediates
        # put push little baby registers here
        
        #POP's------------------------------------------------------------------------------------------
        '8f': 'pop',                                            #POP    NOT DONE
        '58': 'pop', '59': 'pop', '5a': 'pop', '5b': 'pop',     #POP    DONE
        '5c': 'pop', '5d': 'pop', '5e': 'pop', '5f': 'pop',     #       ... cont
        # put pop little baby registers here
        
        #POP CNT------------------------------------------------------------------------------------------
        'f3': 'popcnt',          # 3 bytes f3 0f b8             #POPCNT     NOT DONE
        
        #RET ------------------------------------------------------------------------------------------
        'c3': 'ret',                                            #ret near           READY TO TEST
        'cb': 'ret',                                            #ret far            READY TO TEST
        'c2': 'ret',                                            #ret near  imm      READY TO TEST
        'ca': 'ret',                                            #ret far   imm      READY TO TEST
        
        # XORs ------------------------------------------------------------------------------
        '34': 'xor', '35': 'xor',                               #XOR eax's with an immediate    ?
        '80': '???', '81': '???', '83': '???',                  #XOR r/m with immediate         ?
        '30': 'xor', '31': 'xor',                               #XOR r/m to r                   DONE
        '32': 'xor', '33': 'xor',                               #XOR r  to  r/rm                ?
    }
    
    #iterate through all the opt codes
    for key in optCode:
        
        #if the byte is equal to the key set modify appropriate field in instruction dictionary
        if byte == key:
            
            #keep track of the bytes for the instruction, add the current byte to it
            instruction['Bytes'] = byte
            instruction['Valid'] = True
            #print 'Debug: \t\t VALID INSTRUCTION FOUND!'
            
            
            #set the instruction to the appropriate value (xor add etc)
            instruction['OptCode'] = optCode[key]
            #print 'debug: opt code found!'
            
            #NOTE FIX THIS LATER!!!!!!!!!!!!!!!
            if (key == '???'):
                instruction['Done'] = True
                instruction['src'] = '?'
                instruction['dst'] = '?'
                instruction['Valid'] = False    #temp bug fix
                
            if key =='ff':
                instruction['Done'] = True
                instruction['src'] = '?'
                instruction['dst'] = '?'
                instruction['Valid'] = False
            
            #could be multiple instructions use mod/rm function to calulate op code
            if(key == '81'):
                instruction['Done'] = False
                instruction['OpEn'] = 'MI'
                
                #examine this later!!!!!
                instruction['complexOp'] = True  #more than 1 byte needed to id opt code
                
            
            
            # -----------  ADDs  --------------
            if (key == '01' or key == '02'):
                #   r/m   to r 
                instruction['opEn'] = 'MR'        
                instruction['Done'] = False
                instruction['modrm'] = True
                
            if (key == '03' or key == '04'):
                #   r  and r/m
                instruction['opEn'] = 'RM'        
                instruction['Done'] = False
                instruction['modrm'] = True
                
            
            #MORE ADD HERE
            
            #----------- ANDs ------------------   
            if (key == '20' or key =='21'):
                instruction['Done'] = False
                instruction['opEn'] = 'MR'
                instruction['modrm'] = True
                
                
            if (key == '22' or key == '23'):
                instruction['Done'] = False
                instruction['opEn'] = 'RM'
                instruction['modrm'] = True
                
                            
            #MORE AND HERE
            
            
            #------------ call -----------------
            if (key == 'e8'):
                instruction['Done'] = False
                instruction['modrm'] = False #next 4 bytes are for label
                
            
            #----------- CMP ------------------   
            #CMP r with r/m     MR
            if (key == '38' or key == '39'):
                instruction['Done'] = False
                instruction['opEn'] = 'MR'
                instruction['modrm'] = True
                
            #CMP r/m with r 
            if (key == '3a' or key == '3b'):
                instruction['Done'] = False
                instruction['opEn'] = 'RM'
                instruction['modrm'] = True
                
            
            
            #------------------ jcc --------------------------
            #for 8bit / 1byte jcc's  
            if instruction['OptCode'] == 'jcc':
                instruction['Done'] = False
                instruction['modrm'] = False
                
            #--------------------- lea -------------------------------
            if instruction['OptCode'] == 'lea':
                instruction['Done'] = False
                instruction['modrm'] = True #2nd byte needed for the register
                instruction['opEn'] = 'RM'
                instruction['Length'] = 5   #for reading in the immediate value

            #-------------         MOV 's         -----------------------
            #MOV    REG    REG    
            #for mov register, set done to false so modrm function runs next
            if (key == '88' or key == '89'):
                instruction['opEn'] = 'MR'
                instruction['Done'] = False
                instruction['modrm'] = True
                
            # MOV    R/M     R set down to false so modrm function runs next
            if (key == '8b' or key == '8a'):
                instruction['opEn'] = 'RM'
                instruction['Done'] = False
                instruction['modrm'] = True
            
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
           
            
            
            
            #---------------- POP  ----------------------
            if (instruction['OptCode'] == 'pop'):
               instruction = calculatePop(byte, instruction)
               
            #---------------PUSH----------------
            #for push calculate register being modified
            if (instruction['OptCode'] == 'push'):
                instruction = calculatePush(byte, instruction)
            
            
            
            #---------------- RET -------------------------
            if (key =='cb'or key == 'c3'):
                instruction['src'] = ''
                instruction['dst'] = ''
                instruction['Done'] = True
                
            if (key == 'c2' or key == 'ca'):
               instruction['dst'] = ''               
               instruction['modrm'] = False #to skip over code for processing modrm byte
               #will now return back to previous function, seet hat it is retn and done = false
               #thenw ill read in next 2 bytes
               instruction['Done'] = False            
            
            # ------------ XOR ----------------------------
            #set appropriate opEn mode here    XOR specific
            if (key == '30' or key == '31'):
                instruction['opEn'] = 'MR'
                instruction['Done'] = False
                instruction['modrm'] = True
                
            
            
            
            return instruction
        
     
    
    #at this point we've itterated trhough all values, must not be valid   
    instruction['Valid'] = False
    instruction['Done'] = True
    instruction['Bytes'] = byte
    instruction['OptCode'] = 'Invalid Instruction'
    return instruction



''''
def indentifyComplextOpCode(byte, instruction):
    
    regLookup = {
        '000': 'add', 
    }
    
    #if the 2nd byte is 00 -07 its 
'''



#---------------------------------------------------------------------------
#FunctionName       identifyModrm
#paramaters         instruction = dict of current instruction values
#returns            n/a
#notes              -takes the byte and converts to bits (string)
#                   -breaks up the bits to specific values: modBits, dstBits, srcBits

#---------------------------------------------------------------------------
def indentifyModrm(byte, instruction):
    #print 'got here2'
    regLookup = {
        '000': 'add',   # /0
        '100': 'and',   # /4
    }

    #add byte to the instruction
    instruction['Bytes'] = instruction['Bytes'] + " " + byte
    
    #get byte value to bits
    bits = int(byte, 16)
    bits = bin(bits)    
    savedBits = bits        #for debug
    
    #strip off the 0b and store rest in a list
    bits = bits[2:]
    
    #check for size of bits
    if len(bits) < 8:
        bits = bits.zfill(8)
    
    #determine mod, remove those bits from string bits
    modBits = bits[:2]
    instruction['mod'] = modBits
    bits = bits[2:]
    
    #regbits
    regBits = bits[:3]
    bits = bits[3:]
    
    #2nd operand src
    rmBits = bits[:3]
    
    #for debugging
    #print modBits + " " + regBits + " " + rmBits + " "
    
    
    #the reg bits identify the opCode
    if instruction['opEn'] == 'MI':
        for x in regLookup:
            if regBits == x:
                instruction['OptCode'] = regLookup[x]
                instruction['src'] = bitsToRegister(rmBits)
                instruction['dword'] = True
                return instruction  #leave this functoin early
    
    
    #MR = 1st operand goes in r/m and 2nd goes in reg
    if instruction['opEn'] == 'MR':
        instruction['dst'] = bitsToRegister(rmBits)
        instruction['src'] = bitsToRegister(regBits)
        
    if instruction['opEn'] == 'RM':
        instruction['src'] = bitsToRegister(rmBits)
        instruction['dst'] = bitsToRegister(regBits)
        
        #for handling lea
        if instruction['OptCode'] == 'lea':
            instruction['src'] = None
            instruction['immediate'] = True
            instruction['modrm'] = False
            instruction['mod'] = None # the mod bits are showing up as 00 which would casue [] that isnt right?
            return instruction
    
    
    #The operand is a direct register access.    
    if (modBits == '11'):  
        instruction['Done'] = True
    
    #for memory access in regs   the '[] ' is done in the print instruction function 
    elif (modBits == '00'):
        instruction['Done'] = True
        
    # [reg + dword]
    elif modBits =='10':
        #instruction['src'] = "[ " + bitsToRegister(srcBits) # + some value here 
        #instruction['dst'] = bitsToRegister(dstBits)
        instruction['Done'] = False
        instruction['dword'] = True     #causes it to read in 4 bytes (double check)
        
        #debugs
        #print
        #print 'byte: ' + byte +  '   binaryValue: ' + savedBits,
        #print '   modBits: ' + modBits + ' regBits: '+ regBits+ '  rmBits: ' + rmBits    
    
    elif modBits == '01':
       # instruction['dst'] = bitsToRegister(dstBits)
       # instruction['src'] = "[ " + bitsToRegister(srcBits)
        instruction['Done'] = False
        instruction['byte'] = True
     
        
        #for debug
        #print " " + instruction['dst'] + " " + instruction['src'] + " "
        
        
    # [reg + byte case]
    
    return instruction
    


#---------------------------------------------------------------------------
#FunctionName       printInstruction
#paramaters         instruction = dict of current instruction values
#returns            n/a
#notes
#
#                   to add
#                   - comma's after dst, src
#                   To fix
#                   -why is the ret address off by 1 space?
#---------------------------------------------------------------------------
def printInstruction(instruction):


        #print 'debugging -----------------'
        #print instruction
        #tt = raw_input()
        
        print str(hex(instruction['Location'])) + ":\t",
        print instruction['Bytes'].ljust(16),
        print "\t" + instruction['OptCode'].ljust(6),
        
        
        
        #if instruction['mod'] == '11':
        
        if instruction['mod'] == '01':
            instruction['src'] = "[ " + instruction['src']
            
        elif instruction['mod'] == '10':
            instruction['src'] = "[ " + instruction['src'] 
        
        elif instruction['mod'] == '00':     
            instruction['src'] = "[ " + instruction['src'] + " ]"
            instruction['dst'] = "[ " + instruction['dst'] + " ]"
        
        
        # UGLY RUSHED PRINTING STATEMETNS BELOW....can you tell this was coded on week3 of working on this
        if (instruction['dst'] != None and instruction['dst'] != '') and instruction['OptCode'] != 'ret' and instruction['src']!= '':
            print instruction['dst'] + ",",
            #print "IF A RAN"
            #print 'DEBUG'
            #print instruction
            
        elif instruction['dst'] != None and (instruction['OptCode'] == 'ret'):
            print instruction['dst'], 
            #print "IF B RAN"
          
        elif instruction['dst'] != None and instruction['OptCode'] != 'jcc':
            print instruction['dst'],
            #print "IF C RAN"
            
        print instruction['src'],
            
        print 
        #tt =raw_input() #debug


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
#functionName:      calculatePop
#Paramters          instruction -dictionary conntaing attributes of current instruction
#returns            instruction
#notes              #for popping vlaues off the stack
def calculatePop(byte, instruction):
    
    if (byte == '58'):
        instruction['dst'] = 'eax'
        
    elif (byte == '59'):
        instruction['dst'] = 'ecx'
        
    elif (byte == '5a'):
        instruction['dst'] = 'edx'
    
    elif (byte == '5b'):
        instruction['dst'] = 'ebx'
    
    elif (byte == '5c'):
        instruction['dst'] = 'esp'
        
    elif(byte == '5d'): 
        instruction['dst'] = 'ebp'
    
    elif(byte == '5e'):
        instruction['dst'] = 'esi'
        
    elif (byte == '5f'):
        instruction['dst'] = 'edi'
        
    else:
        instruction['dst'] = 'ERROR'
    
    #so code moves to next instruction
    instruction['Done'] = True
    instruction['src'] = ''
    
    return instruction



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
        instruction['dst'] = 'esi'
        
    elif (byte == '57'):
        instruction['dst'] = 'edi'
        
    else:
        instruction['dst'] = 'ERROR'
    
    #so code moves to next instruction
    instruction['Done'] = True
    instruction['src'] = ''
    
    return instruction
    
    

#---------
# jmp stuff

# first opt is 74
# 74 = 255 - 7 -2 


#calculate jmp lable is
#current position + sizeofInstruction + byte(in hex)



# starting address       = 7
# instruction length     = 2
# value that follows it? = 15



''''
74   04    
    04 represents the end of the instruciton and wher eits going to jump
    location = starting address + instruction length + value that follows it  ****    
                010073A5  + 2 + 4

                010073AB
                
                starting address is EIP
                max jump forward is eip + 2 + 0x7f  (postive 81 adding to eip)

    
    
    cmp
    jz      jret   #on code block being set
    CODE 2
    JRET: retn
    
if you wanted to jump 128
opcode is 0f 84 imediate
0f  84  80 00 00 00          caue it takes cd which is a dword

eip + 6 + 0x80

        6 is the instruction length
    
''' 
    
    
main();
