#!/bin/sh

#====================================================================
# This script requires the use of socat.  If this is not installed
# then install it using sudo apt-get socat
#====================================================================

function loopback {

    echo ======================================================
    echo         Setting up serial loopback
    echo ======================================================
    echo $1

    #find the last used USB tty device
    lastUSB=  $(ls /dev/ttyUSB* | awk '{ print length(), $0 | "sort -n" }' | sort -r | sed 's/\(\/dev\/ttyUSB\)\(.*[0-9]\)/\2/' | awk '{ print $2 }')
    if [ -z $lastUSB ]
    then
        lastUSB='0'
    fi


    ##create the two linked pseudoterminals
    (socat -d -d pty,raw,echo=0 pty,raw,echo=0 2>&1 | tee /tmp/socatConnectors)&
    sleep 2
    for i in $( cat /tmp/socatConnectors | grep "/dev/pts" |head -2 /tmp/socatConnectors | awk '{ print $7 }')
    do
        ((lastUSB++))
        echo "linking /dev/ttyUSB"$lastUSB "to " $i
        echo "/dev/fttyUSB"$lastUSB >> $1/serial.properties
        ln -s $i  "/dev/ttyUSB"$lastUSB
    done
    rm /tmp/socatConnectors
}

function removeLoopback {
    killall socat
    for i in $(cat serial.properties)
    do
        unlink $i
    done
    rm $1/serial.properties
}


echo "============================================================"
echo "                  Serial port loopback                      "
echo "============================================================"
if [ $1 == "link" ]
then
    echo " Generating Loopback "
    loopback $2
elif [ $1 == "unlink" ]
then
    echo " Removing loopback "
    removeLoopback $2
fi
echo "============================================================"
echo "              Serial port loopback Complete                 "
echo "============================================================"


