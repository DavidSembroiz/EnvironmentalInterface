import java.io.*;
import java.net.*;

import org.json.simple.parser.*;
import org.json.simple.JSONObject;

//--------------- Classes for reading from Virtual Sensors via Sockets ------------//

public class VirtualReader extends Thread {
    
    private int port;
    private ServerSocket serverSocket;
    private Database awsdb;
    
    public VirtualReader(int p, Database awsdb) {
        port = p;
        this.awsdb = awsdb;
    }
    
    public void run() {
        System.out.println("Virtual reader server running");
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port));
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Runnable c = new Client(clientSocket, awsdb);
                new Thread(c).start();
            }
        } 
        catch(IOException e) { System.out.println(e.getMessage()); }
    }

    private class Client implements Runnable {
        private final Socket clientSocket;
        private Utils uts;
		private Database awsdb;
        private File f;

        private Client(Socket c, Database awsdb) {
            clientSocket = c;
			this.awsdb = awsdb;
        }

        private JSONObject parseMessage(String[] data) {
            try {
            
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(new FileReader(f));
                JSONObject channels = (JSONObject) obj.get("channels");
                obj.put("lastUpdate", System.currentTimeMillis());
                for (int i = 1; i < data.length; ++i) {
                    String[] sensor = data[i].split(":");
                    JSONObject o = (JSONObject) channels.get(sensor[0]);
                    if (sensor[0].equals("temperature")) {
                        o.put("current-value", Double.parseDouble(sensor[1]));
                    }
                    else if (sensor[0].equals("luminosity")) {
                        o.put("current-value", Integer.parseInt(sensor[1]));
                    } 
                    else if (sensor[0].equals("humidity")) {
                        o.put("current-value", Double.parseDouble(sensor[1]));
                    }    
                }
                return obj;
            } catch(ParseException e) { e.printStackTrace(); }
              catch(IOException e) { 
                System.out.println("Data file not found");
            }
            return null;
        }
        
        @Override
        public void run() {
            try {
                BufferedReader clientData = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = clientData.readLine();
                
                String[] data = message.split(";");
                int id = Integer.parseInt(data[0]);

                uts = new Utils(id, awsdb);
                awsdb.assignUtils(uts);
                f = uts.openDatafile();
                
                JSONObject obj = parseMessage(data);
                if (obj != null) {
                    uts.writeToDataFile(obj.toJSONString());
                    uts.pushData();
                    clientSocket.close();
                }
            }
            catch(IOException e) { e.printStackTrace(); }
        }
    }
}
