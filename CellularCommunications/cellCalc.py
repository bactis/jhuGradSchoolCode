###########################################################
# Author:		Ben Actis
# Date			6/7/2014
# Notes:		Calculates equations for class


import math

#-------------------------------------------------------------------------------------
#Name               main
#Params:            n/a
#Returns:           n/a
#Notes:             presents a menu for the user to choose different calculations
#                   also displays current calculation values
def main():

        #variables
        lightUrbanLoss = 0.0
        rss            = 0.0 # receivedSignalStrength

        #loop for menu. Continues until user enters q
        done = False
        while done == False:
            print "*************************************************"
            print "         welcome to the lazy hw helper           "
            print ""
            print " 1: calculate light urban loss \t current light urban loss: " + \
                    str(lightUrbanLoss)
            print " 2: calcReceivedSignalStrength \t current value is: " + \
                    str(rss)
            print " 3: calculate recieved power by modifying hbs"

            print " q: quit"
            selection = raw_input("Selection: ")
            print "*************************************************"
            
            #if user wants to calculate light urban loss
            if selection == "1":
                lightUrbanLoss = calcLightUrbanLoss()

            # if user wants to calculate light urban loss
            elif selection == "2":
                rss = calcReceivedSignalStrength(lightUrbanLoss)

            #if user wants to quit
            elif selection == "q":
                exit()
                done = True

#----------------------------------------------------------------------------------------
#Name:          calcLightUrbanLoss
#Params:        n/a
#Returns:       float light urban loss
#Notes:         - prompts user for needed variables (hbs, hmobile, distance, and freq)
#               - performs calculation
#               - also prints out steps on screen
#               - returns value
def calcLightUrbanLoss():

	# ------------------variables
	hbs     = 0.0 	#effective  base station antenna hight (m)
	hMobile = 0.0	#mobile antenna height (m)
	d       = 0.0	#distance between BS and mobile (KM)
	f       = 0.0	#frequency in MHz
        
	# get variables from user and typecast to a float for math.log to work
	print ""
        print "Enter hbs: ",
	hbs = raw_input()
        hbs = float(hbs)

        #get hMobile
	print "Enter hMobile: ", 
	hMobile = raw_input()
        hMobile = float(hMobile)

        #get frequency
	print "Enter frequency",  
	f      = raw_input()
        f = float(f)
	
        #get distance
	print "Enter distance: ", 
	d      = raw_input()
        d = float(d)

	#calculate light urban loss....step by step
        print ""
	print "calculating light urban loss.."
        print "46.3 + (33.9 * log(f) - 13.82log(hbs) - "
        print "(1.1log(f) - 0.7)hmobile + 1.56log(f) - 0.8 + (44.9 - 6.55"
        print "log(hbs((log(d)"
        print ""

        #variables for calculation
        pt1 = 33.9 * math.log10(f)
        pt2 = (-13.82 * math.log10(hbs))
        pt3 = -1* ((1.1 * math.log10(f) - 0.7) * hMobile )
        pt4 = (1.56 * math.log10(f))
        pt5 = (44.0 - (6.55 * math.log10(hbs))) * math.log10(d)

        #print next step and type cast pt1-5 as strings
        print "46.3 + " + str(pt1) + " " + str(pt2) + " " + str(pt3) + " + " + str(pt4) \
                + "- 0.8 - " + str(pt5)

        #do final calculation
	lightUrbanLoss = 46.3 + (33.9 * math.log10(f)) - (13.82 * math.log10(hbs)) - \
                ((1.1 * math.log10(f) - 0.7) *hMobile) + (1.56 * math.log10(f)) - 0.8 + \
                (44.9 - 6.55 * math.log10(hbs) )  * math.log10(d)
	print "lightUrbanLoss is: " + str(lightUrbanLoss) + " dB"
        print ""
        return lightUrbanLoss

#-------------------------------------------------------------------------------
#Name:          calcReceivedSignalStrength
#Params:        float pathLoss
#Returns:       float recieved signal strength
#Notes          - prompts user for EIRP,
#               - subtracts path loss from EIRP
#               - shows steps for completion
def calcReceivedSignalStrength(pathLoss):
    
    #show basic equation
    print ""
    print "Received signal strength (dBm) = EIRP (dBm) - PathLoss(dBM)"

    #get IERP
    print "Enter EIRP:",
    eirp = raw_input()
    eirp = float(eirp)
    
    print ""
    print "calculation is below..."
    print str(eirp) + "  " + str(( -1 * (pathLoss))) 
    print ""

    print "received signal strenght = " + str(eirp - pathLoss) + " dBM"
    return eirp - pathLoss



def findTheHBS():
    
    print ""
    print "this method calculates the hbs if recieved signal strength and EIRP"
    print ""

    #get EIRP from user
    print "Enter EIRP: ",
    eirp = raw_input()
    
    #recived signal strength from user
    print "Enter recieved signal strength"
    rss = raw_input()

    #show work
    print "calculating HBS equation for hbs"
    print "recieved signal strength = EIRP - PATHLOSS"
    print "recieved signal strength - EIRP = - PATHLOSS"
    print "- recieved signal strength + EIRP = PATHLOSS"
    print "PATHLOSS = recieved signal strength - EIRP"
    print "PATHLOSS = " + str(rss) + " - " + str(eirp)
    pathloss = float(rss) - float(eirp)
    
     
    # get variables from user and typecast to a float for math.log to work
    print ""

    #get hMobile
    print "Enter hMobile: ", 
    hMobile = raw_input()
    hMobile = float(hMobile)

    #get frequency
    print "Enter frequency",  
    f      = raw_input()
    f = float(f)
	
    #get distance
    print "Enter distance: ", 
    d      = raw_input()
    d = float(d)

    #calculate light urban loss....step by step
    print ""
    print "calculating hbs...."
    print str(pathloss) + " = 46.3 + 33.9log(f) - 13.82log(hbs) -(1.1log(f) - 0.7)hmobile + 1.56log(f) - 0.8 + (44.9 - 6.55log(hbs))(log(d)"
    print str(pathloss) + " -46.3 = ..."
    print str(pathloss) + " -46.3 - 33.9log(f) = ..."
    print str(pathloss) + "-46.3 -33.9log(f) + (1.1log(f) - 0.7)hmobile = -13.82(hbs) + ..."
    print str(pathloss) + "-46.3 -33.9log(f) + (1.1.log(f) -0.7)hmobile -1.56log(f) = 13.82(hbs) - ..."
     print str(pathloss) + "-46.3 -33.9log(f) + (1.1.log(f) -0.7)hmobile -1.56log(f) = 13.82(hbs) + 0.8 = -13.82log(hbs) + (44.9 -6.55log(hbs)) log(d)





    #print "-84 = 60 - PATHLOSS"
    #print "-84 + passloss = 60"
    #print "pathloss = 84"

    


main()
#calcLightUrbanLoss() 
