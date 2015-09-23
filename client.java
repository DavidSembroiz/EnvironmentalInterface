
import java.io.*;
import java.net.*;
import java.util.*;

public class client {
 
	public static void main(String argv[]) throws Exception {

        int port = -1;
        int id = -1;
        boolean reg = false;
        String mode = "";
        
        if (argv.length == 4 || argv.length == 5 || argv.length == 6 || argv.length == 7) {
            if (argv[0].equals("-port") && argv[2].equals("-id")) {
	          port = Integer.parseInt(argv[1]);
              id = Integer.parseInt(argv[3]);
	        }
            else {
		      System.exit(1);
	        }
            if (argv.length == 5 || argv.length == 7) {
                if (argv[4].equals("-regonly") && argv[5].equals("-mode")) {
                    reg = true;   
                    mode = argv[6].toLowerCase();         
                }   
                else System.exit(1); 
            }
            if (argv.length == 6) {
                if (argv[4].equals("-mode")) {
                    mode = argv[5].toLowerCase();            
                }   
                else System.exit(1); 
            }
        }
        else {
            System.out.println("Usage: java client [-port <PORT_ADDRESS> -id <LOCAL_ID>] [-regonly]");
            System.exit(1);        
        }

        Random rand = new Random();
        int sent = 0;

	    while (true) {
            
            double temp = rand.nextDouble() * 40;
            int light = rand.nextInt(2000);
            double hum = rand.nextInt(10000) / 100.0;

            String send = "";
            if (mode.equals("computer")) send = id + ";computer:off";
            else if (mode.equals("xm1000")) send = id + ";temperature:" + temp + ";luminosity:" + light + ";humidity:" + hum;

		    Socket clientSocket = new Socket("localhost", port);
		    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		    outToServer.writeBytes(send + '\n');
            clientSocket.close();
            ++sent;
            if (reg && sent >= 3) System.exit(1);
            Thread.sleep(5000 + rand.nextInt(50) * 100);
        }
    }
}
