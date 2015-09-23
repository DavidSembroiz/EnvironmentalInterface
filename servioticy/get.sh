#!/bin/bash

[ $# -ne 2 ] && { echo "Usage: $0 <API_KEY> <Service Object ID>"; exit 1; } 

key=$(cat $1)
ide=$(cat $2)

cr=$'\r'
ide="${ide%$cr}"

curl -i -H "Content-Type: application/json" \
-H "Authorization: ${key}" http://api.servioticy.com/${ide}/streams/data/lastUpdate
