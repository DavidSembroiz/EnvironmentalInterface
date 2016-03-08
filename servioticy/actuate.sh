#!/bin/bash

[ $# -ne 3 ] && { echo "Usage: $0 <API_KEY> <Service Object ID> <action file>"; exit 1; } 

key=$(cat $1)
ide=$(cat $2)

action_params=()
while IFS='' read -r line || [[ -n "$line" ]]; do
    action_params+=("$line")
done < "$3"

cr=$'\r'
ide="${ide%$cr}"

curl -i -X POST -H "Content-Type: text/plain" \
-d ${action_params[1]} \
-H "Authorization: ${key}" http://api.servioticy.com/${ide}/actuations/${action_params[0]}
