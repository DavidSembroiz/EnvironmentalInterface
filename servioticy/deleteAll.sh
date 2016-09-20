#!/bin/bash

[ $# -ne 0 ] && { echo "Usage: $0 <API_KEY>"; exit 1; } 

key=$(cat "./res/key")

for file in "./res/id"*; do
    
    ide=$(cat $file)

    cr=$'\r'
    ide="${ide%$cr}"

    curl -i -X DELETE -H "Content-Type: application/json" \
    -H "Authorization: ${key}" aledo.ccaba.upc.edu:8080/${ide}

    psql -U upc -h aledo.ccaba.upc.edu -d data -c "DELETE FROM ids WHERE servioticy_id ='${ide}';"

    number=$(echo $file | tr -dc '0-9')

    rm res/id${number}

done



