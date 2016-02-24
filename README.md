# EnvironmentalInterface

Underlying middleware for ServIoTicy.

# Requirements

* ServIoTicy APIKEY inside `/servioticy/res` directory.
* secrets.example must be fulfilled, instructions inside.

## Deletion Requirements

In order to use the delete scripts to clean Service Objects, additional tools are needed:

* cURL
* PostgreSQL client 
`sudo apt-get install postgresql-common postgresql-client-<version>`
* File .pgpass in root directory with the following database info content
```
cd ~
echo "hostname:port:dbname:dbuser:dbpassword" > .pgpass
```

# Preparation Phase

In order to create a valid `/servioticy` folder, several files must be created before executing. Firstly, models for room sensors must be created. To do so, make use of the generator inside `/servioticy/models`

```
javac modelGenerator.java
java modelGenerator #rooms
``` 

Each room is currently composed of 5 sensors (XM1000, Light, Power, Presence and Air quality).

Additionally, actuators can also be created by making use of the generator inside `/servioticy/actuators`

```
javac actuatorGenerator.java
java actuatorGenerator #rooms
```

Currently, each room contains Computer, Light and HVAC actuators. In order to push the actuators to ServIoTicy and store the IDS into the database, additional scripts are created.

```
./push_actuators.sh
```

The script firstly uses the `create_SO.sh` script to communicate with ServIoTicy and obtain the IDs. Finally, it uses the script `db.py` to store the IDs into the database.

# Usage

There are several options to launch the Interface. Currently, virtual, real or both sensor servers can be run. If the real sensor server is needed, it is firstly required to start the serial forwarder. At the moment, only XM1000 motes are supported. For more driver and specs information refer to http://www.advanticsys.com/shop/asxm1000-p-24.html 

```
./start_sf.sh (only if the serial port is being used)
java -jar EnvIface.jar [-comm <source>] [-port <port>]
```

Once the servers are running, if virtual sensors are needed, they are executed as follows:

```
java sensors [-port <port>] [-ids <#sensors>]
```

Either of those commands will execute the sensors, sending 2 messages for each of them.


