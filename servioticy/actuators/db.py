#/usr/bin/python2.4

import psycopg2

# Try to connect

try:
    conn=psycopg2.connect("dbname='aledo.ccaba.upc.edu:5432' user='upc' password='user'")
except:
    print "I am unable to connect to the database."

cur = conn.cursor()
try:
    cur.execute("INSERT INTO actuators VALUES(%s, %s)", ())
except:
    print "unable to execute"

