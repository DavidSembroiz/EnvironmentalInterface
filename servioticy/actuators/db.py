#/usr/bin/python2.4

import psycopg2
import glob, os, json

# Try to connect

try:
    conn=psycopg2.connect("host='aledo.ccaba.upc.edu' dbname='data' user='upc' password='user'")
    cur = conn.cursor()
    
except:
    print "Unable to connect to the database."

for file in glob.glob("id*"):
    soid = json.loads(open(file).readline())["id"]
    json_model = json.loads(open("actuator_" + file[2:] + ".json", "r").readline())
    model = json_model["customFields"]["model"]
    location = json_model["customFields"]["location"]
    try:
        cur.execute("INSERT INTO actuators (servioticy_id, model, location) VALUES(%s, %s, %s)", (soid, model, location))
    except:
        print "Unable to insert into the database"

conn.commit()
cur.close()
conn.close()

