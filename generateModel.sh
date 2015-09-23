#!/bin/bash

[ $# -ne 2 ] && { echo "Usage: $0 <message header file> <output classname>"; exit 1; } 

mig java -target=null -java-classname=$2 $1 $2 -o $2.java
