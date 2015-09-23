#!/bin/bash

[ $# -ne 1 ] && { echo "Usage: $0 #clients"; exit 1; } 

mkdir -p /tmp/servioticy

N=$1

for ((i=1; i<=N; i+=2)); do
        java client -port 9090 -id $i -mode xm1000 &
        sleep 1s
        echo $! > /tmp/servioticy/process${i}.pid
done

for ((i=2; i<=N; i+=2)); do
        java client -port 9090 -id $i -mode computer &
        sleep 1s
        echo $! > /tmp/servioticy/process${i}.pid
done
