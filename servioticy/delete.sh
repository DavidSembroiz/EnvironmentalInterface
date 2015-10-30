#!/bin/bash

[ $# -ne 2 ] && { echo "Usage: $0 <API_KEY> <Service Object ID>"; exit 1; } 

key=$(cat $1)
ide=$(cat $2)

cr=$'\r'
ide="${ide%$cr}"

curl -i -X DELETE -H "Content-Type: application/json" \
-H "Authorization: ${key}" http://api.servioticy.com/${ide}

psql -U upc -h aledo.ccaba.upc.edu -d data -c "DELETE FROM ids_pfm WHERE servioticy_id ='${ide}';"

number=${2%[!0-9]*}
number=${2##*[!0-9]}

rm res/id${number}

