#/bin/bash/sh -e

#-----------------------------------------------------------------------------#
### This will show the requirement of the script is there are not enough
### parameters defined.
#-----------------------------------------------------------------------------#

if [ -z "$2" ]
  then
    echo "Usage: devops_adl.sh  <host URL> <user>"
    echo ""
    echo "host url: This is the URL used when connecting to the GOBii LoaderUI"
    echo "it contains the hostname, port and crop"
    echo "EXAMPLE: http://cbsugobii10.tc.cornell.edu:8081/gobii-dev/"
    echo ""
    echo "user: Username used when logging into GOBii LoaderUI or Extactor"
    echo ""
    echo "pass: The password used when logging in with the previously entered"
    echo "username."

    exit
fi

host=$1
username=$2

stty -echo # turns off terminal echo
read -p "User Password: " userpass; echo
stty echo # turns terminal echo back on


java -jar /data/gobii_bundle/core/gobiiadl.jar -h $host -u $username -p $userpass -d .

