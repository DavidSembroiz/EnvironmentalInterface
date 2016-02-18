#!/bin/bash

[ $# -ne 1 ] && { echo "Usage: $0 <API_KEY>"; exit 1; }

key=$(cat $1)

for file in "./actuator_"*; do

    id=$(curl -i -X POST -H "Content-Type: application/json" \
    -H "Authorization: ${key}" -d @$file http://api.servioticy.com/)

    number=$(echo $file | tr -dc '0-9')


    echo "$id" > id$number

done


