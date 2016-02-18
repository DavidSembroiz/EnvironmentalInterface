#/usr/bin/python2.4

#import psycopg2
import glob, os, json

# Try to connect

try:
    print("mock")
    #conn=psycopg2.connect("dbname='aledo.ccaba.upc.edu:5432' user='upc' password='user'")
except:
    print "I am unable to connect to the database."

for file in glob.glob("id*"):
    soid = open(file).readline()
    model = open("actuator_" + file[2:] + ".json", "r").readline()
    json_model = json.loads(model)
    mod = json_model["customFields"]["model"]
    location = json_model["customFields"]["location"]
    print(soid)
    print(mod)
    print(location)

#cur = conn.cursor()
#try:
#    cur.execute("INSERT INTO actuators VALUES(%s, %s, %s)", (soid, mod, location))
#except:
#    print "unable to execute"

