#!/bin/bash

[ $# -ne 0 ] && { echo "Usage: $0"; exit 1; }


key="`cat ../res/key`"

for file in "./actuator_"*; do

    number=$(echo $file | tr -dc '0-9')

    if [ ! -f id$number ]; then

        id=$(curl -s -X POST -H "Content-Type: application/json" \
        -H "Authorization: ${key}" -d @$file http://api.servioticy.com/)

        echo "$id" > id$number
    fi
done


