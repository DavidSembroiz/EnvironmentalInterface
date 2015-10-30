#!/bin/bash

[ $# -ne 1 ] && { echo "Usage: $0 <API_KEY>"; exit 1; } 

key=$(cat $1)

for file in "./res/id"*; do
    
    ide=$(cat $file)

    cr=$'\r'
    ide="${ide%$cr}"

    curl -i -X DELETE -H "Content-Type: application/json" \
    -H "Authorization: ${key}" http://api.servioticy.com/${ide}

    psql -U upc -h aledo.ccaba.upc.edu -d data -c "DELETE FROM ids_pfm WHERE servioticy_id ='${ide}';"

    number=$(echo $file | tr -dc '0-9')

    rm res/id${number}

done



