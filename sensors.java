
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class sensors {
 
	public static void main(String argv[]) throws Exception {

        /**
        * number of ids to be executed
        */
        int IDS = 10;

        int port = -1;
        int id = -1;
        boolean reg = false;
        String mode = "";

        
        int THREADS = 50;
        if (argv.length == 4) {
            if (argv[0].equals("-port")) {
                port = Integer.parseInt(argv[1]);
            }
            if (argv[2].equals("-ids")) {
                IDS = Integer.parseInt(argv[3]);
            }
        }
        else {
            System.out.println("Usage: java client [-port <PORT_ADDRESS>] [-ids <#IDS>]");
            System.exit(1);        
        }

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        for (int i = 1; i <= IDS; ++i) {
            if (i%5 == 1) mode = "xm1000";
            else if (i%5 == 2) mode = "light";
            else if (i%5 == 3) mode = "presence";
            else if (i%5 == 4) mode = "power";
            else mode = "airquality";

            Runnable worker = new Client(i, mode, port);
            executor.execute(worker);
        }
        executor.shutdown();
        while(!executor.isTerminated()) {
    
        }
        System.out.println("All Threads Finished");
    }

    public static class Client implements Runnable {

		private final int id;
        private final String mode;

        private int sent;
        private String message;
        private Random rand;
        private Socket clientSocket;
        private int port;
 
		Client(int id, String mode, int port) throws Exception {
			this.id = id;
            this.mode = mode;
            this.port = port;
            sent = 0;
            message = "";
            rand = new Random();
		}

        private String lightMessage() {
            int state = rand.nextInt(2);
            String message = id + ";light:" + state;
            return message;
        }

        private String xm1000Message() {
            double temp = rand.nextDouble() * 40;
            int light = rand.nextInt(2000);
            double hum = rand.nextInt(10000) / 100.0;
            String message = id + ";temperature:" + temp + ";luminosity:" + light + ";humidity:" + hum;
            return message;
        }

        private String presenceMessage() {
            int state = rand.nextInt(2);
            String message = id + ";presence:" + state;
            return message;
        }

        private String powerMessage() {
            double state = rand.nextDouble() * 400;
            String message = id + ";power:" + state;
            return message;
        }
 
        private String airQualityMessage() {
            int state = rand.nextInt(3);
            String message = id + ";airquality:" + state;
            return message;
        }

		@Override
		public void run() {
            try {
                while (sent < 2) {
                    Thread.sleep(rand.nextInt(50) * 100);

                    if (mode.equals("xm1000")) message = xm1000Message();
                    else if (mode.equals("light")) message = lightMessage(); 
                    else if (mode.equals("presence")) message = presenceMessage();
                    else if (mode.equals("power")) message = powerMessage();
                    else if (mode.equals("airquality")) message = airQualityMessage();

                    clientSocket = new Socket("localhost", port);
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    outToServer.writeBytes(message + '\n');
                    clientSocket.close();
                    ++sent;
                    Thread.sleep(2000 + rand.nextInt(50) * 100);
                }
            } catch(Exception e) {
                // who cares
            }		
		}
	}
}
