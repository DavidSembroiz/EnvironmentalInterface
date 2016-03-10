#/usr/bin/python2.4

import psycopg2
import glob, os, json


# Try to connect

try:
    conn=psycopg2.connect("host='aledo.ccaba.upc.edu' dbname='data' user='upc' password='user'")
    cur = conn.cursor()
    cur.execute("""CREATE TABLE actuators(servioticy_id VARCHAR(50) PRIMARY KEY, 
                                          model VARCHAR(30),
                                          location VARCHAR(100),
                                          created timestamp default current_timestamp(2));""")
    conn.commit()
    
except psycopg2.Error as e:
    conn.rollback()

for f in glob.glob("id*"):
    soid = json.loads(open(f).readline())["id"]
    json_model = json.loads(open("actuator_" + f[2:] + ".json", "r").readline())
    model = json_model["customFields"]["model"]
    location = json_model["customFields"]["location"]
    try:
        cur.execute("INSERT INTO actuators (servioticy_id, model, location) VALUES(%s, %s, %s)", (soid, model, location))
    except psycopg2.Error as e:
        conn.rollback()
        print("Unable to insert into the database.")
    else:
        conn.commit()

cur.close()
conn.close()

