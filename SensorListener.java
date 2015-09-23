import java.io.*;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;

import org.json.simple.parser.*;
import org.json.simple.JSONObject;

public class SensorListener implements MessageListener {

        private File f;
        private Utils uts;
        private Database awsdb;

        public SensorListener(Database awsdb) {
            this.awsdb = awsdb;
        }

        public JSONObject parseMessage(Message message) {

            SensorMsg msg = (SensorMsg) message;
            
            try {
                JSONParser parser = new JSONParser();
		        JSONObject obj = (JSONObject) parser.parse(new FileReader(f));
		
		        JSONObject channels = (JSONObject) obj.get("channels");
		        JSONObject temp = (JSONObject) channels.get("temperature");
		        JSONObject hum = (JSONObject) channels.get("humidity");
		        JSONObject light = (JSONObject) channels.get("luminosity");
		
		        temp.put("current-value", msg.get_temp()/100.0);
		        hum.put("current-value", msg.get_hum()/100.0);
		        light.put("current-value", msg.get_light());
		        obj.put("lastUpdate", System.currentTimeMillis());

                long id = msg.get_nodeid();
		        double tempVal = (Double) temp.get("current-value");
		        double humVal = (Double) hum.get("current-value");
		        long lightVal = (Long) light.get("current-value");
		        long lastUp = (Long) obj.get("lastUpdate");
                double battVal = msg.get_batt()/100.0;

		        System.out.println("Node: " + Long.toString(id));
		        System.out.println("Temperature: " + Double.toString(tempVal) + " ºC");
		        System.out.println("Humidity: " + Double.toString(humVal) + " %");
		        System.out.println("Luminosity: " + Long.toString(lightVal));
		        System.out.println("Last Update: " + Long.toString(lastUp));
		        System.out.println("Battery: " + Double.toString(battVal) + " V");

                return obj;
                
            } catch(ParseException e) {}
              catch(IOException e) {}
            return null;
        }        


        public void messageReceived(int to, Message message) {

            int source = message.getSerialPacket().get_header_src();
            uts = new Utils(source, awsdb);
	        f = uts.openDatafile();

            JSONObject obj = parseMessage(message);

            if (obj != null) {
                uts.writeToDataFile(obj.toJSONString());
	            uts.pushData();
            }
        }
    }  
