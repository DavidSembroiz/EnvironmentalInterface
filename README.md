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

# Usage

There are several options to launch the Interface. Currently, virtual, real or both sensor servers can be run. If the real sensor server is needed, it is firstly required to start the serial forwarder. At the moment, only XM1000 motes are supported. For more driver and specs information refer to http://www.advanticsys.com/shop/asxm1000-p-24.html 

```
./start_sf.sh (only if the serial port is being used)
java -jar EnvIface.jar [-comm <source>] [-port <port>]
```

Once the servers are running, if virtual clients are needed, they are executed as follows:

```
./run_clients.sh #rooms
./run_clients_forever.sh #rooms
```

Either of those commands will execute the clients, the former only sends 3 messages per client and the later sends messages forever.


