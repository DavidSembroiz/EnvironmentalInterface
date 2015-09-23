#!/bin/bash

N=$(ls -1 /tmp/servioticy | wc -l)

for ((i=1; i<=N; ++i)); do
        kill -9 `cat /tmp/servioticy/process${i}.pid`
    done

rm -rf /tmp/servioticy
