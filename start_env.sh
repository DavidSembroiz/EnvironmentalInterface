#!/bin/bash

usage="Usage: $0 [comm] [port <#port>]"

[ $# -eq 1 ] && [ $1 != "comm" ] && { echo $usage; exit 1; }
[ $# -eq 2 ] && [ $1 != "port" ] && { echo $usage; exit 1; }
[ $# -ne 1 ] && [ $# -ne 2 ] && [ $# -ne 3 ] && { echo $usage; exit 1; }

[ $# -eq 1 ] && { java EnvInterface -comm sf@localhost\:9002; }
[ $# -eq 2 ] && { java EnvInterface -port $2; }
[ $# -eq 3 ] && { java EnvInterface -comm sf@localhost\:9002 -port $3; }

