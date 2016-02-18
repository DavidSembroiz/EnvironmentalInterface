#/usr/bin/python2.4

import psycopg2
import glob, os, json

# Try to connect

try:
    conn=psycopg2.connect("host='aledo.ccaba.upc.edu:5432' dbname='data' user='upc' password='user'")
    cur = conn.cursor()
except:
    print "I am unable to connect to the database."

for file in glob.glob("id*"):
    soid = json.loads(open(file).readline())["id"]
    json_model = json.loads(open("actuator_" + file[2:] + ".json", "r").readline())
    model = json_model["customFields"]["model"]
    location = json_model["customFields"]["location"]
    print(soid)
    print(model)
    print(location)

try:
    cur.execute("INSERT INTO actuators VALUES(%s, %s, %s)", (soid, mod, location))
except:
    print "unable to execute"

