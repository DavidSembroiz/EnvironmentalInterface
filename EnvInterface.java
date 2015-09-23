import java.io.*;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;

import org.json.simple.parser.*;
import org.json.simple.JSONObject;


public class EnvInterface {

    private MoteIF moteIF;
    private static VirtualReader vreader;
	private static Database awsdb;

    // Define mote listeners
    private XM1000Listener xm1000listen;
    private SensorListener senslisten;

  
    public EnvInterface(MoteIF moteIF) {
        this.moteIF = moteIF;

        // Create and register every listener 
        xm1000listen = new XM1000Listener(awsdb);
        senslisten = new SensorListener(awsdb);

        this.moteIF.registerListener(new SensorMsg(), senslisten);
		this.moteIF.registerListener(new XM1000Msg(), xm1000listen);
    }
  
    private static void usage() {
        System.err.println("usage: demoNew [-comm <source>] [-port <port>]");
    }
  
    public static void main(String[] args) throws Exception {
        String source = null;
	    String port = null;
        if (args.length == 2) {
            if (args[0].equals("-comm")) {
		      source = args[1];
            }
	        else if (args[0].equals("-port")) {
	          port = args[1];
	        }
            else {
		      usage();
		      System.exit(1);
	        }
        }
	
	    else if (args.length == 4) {
		    if (args[0].equals("-comm")) {
			    source = args[1];
          	}
		    else {
			    usage();
			    System.exit(1);
		    }
		    if (args[2].equals("-port")) {
	        	port = args[3];
	        }
		    else {
			    usage();
			    System.exit(1);
		    }
	    }
        if (args.length == 0 && args.length != 2 && args.length != 4) {
          usage();
          System.exit(1);
        }
        
        PhoenixSource phoenix;
        Database awsdb = new Database();

	    if (port != null) {
		    vreader = new VirtualReader(Integer.parseInt(port), awsdb);
		    vreader.start();
		
	    }
        if (source != null) {
            phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
            MoteIF mif = new MoteIF(phoenix);
            EnvInterface serial = new EnvInterface(mif);
        }
    }
}
