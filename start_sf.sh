#!/bin/bash

[ $# -ne 0 ] && { echo "Usage: $0"; exit 1; } 

java net.tinyos.sf.SerialForwarder -comm serial@/dev/ttyUSB0:xm1000 &


