#!/bin/bash

[ $# -ne 3 ] && { echo "Usage: $0 <API_KEY> <Service Object ID> <action>"; exit 1; } 

key=$(cat $1)
ide=$(cat $2)

cr=$'\r'
ide="${ide%$cr}"

curl -i -X POST -H "Content-Type: text/plain" \
-H "Authorization: ${key}" http://api.servioticy.com/${ide}/actuations/$3
