#!/bin/bash

[ $# -ne 1 ] && { echo "Usage: $0 #rooms"; exit 1; } 

mkdir -p /tmp/servioticy

N=$1*2

for ((i=1; i<=N; i+=2)); do
        java client -port 9090 -id $i -regonly -mode xm1000 &
        sleep 0.5s
        echo $! > /tmp/servioticy/process${i}.pid
done

for ((i=2; i<=N; i+=2)); do
        java client -port 9090 -id $i -regonly -mode computer &
        sleep 0.5s
        echo $! > /tmp/servioticy/process${i}.pid
done
