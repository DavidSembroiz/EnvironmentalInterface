#!/bin/bash

[ $# -ne 0 ] && { echo "Usage: $0"; exit 1; }


key="`cat ../res/key`"

for file in "./actuator_"*; do

    id=$(curl -X POST -H "Content-Type: application/json" \
    -H "Authorization: ${key}" -d @$file http://api.servioticy.com/)

    number=$(echo $file | tr -dc '0-9')


    echo "$id" > id$number

done


