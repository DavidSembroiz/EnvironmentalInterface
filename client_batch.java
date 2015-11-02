
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class client_batch {
 
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
            if (i%2 == 0) mode = "computer";
            else mode = "xm1000";
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
            //clientSocket = new Socket("localhost", port);
		}
 
		@Override
		public void run() {
            try {
                while (sent < 2) {
                    Thread.sleep(rand.nextInt(50) * 100);
                    double temp = rand.nextDouble() * 40;
                    int light = rand.nextInt(2000);
                    double hum = rand.nextInt(10000) / 100.0;

                    if (mode.equals("computer")) message = id + ";computer:off";
                    else if (mode.equals("xm1000")) message = id + ";temperature:" + temp + ";luminosity:" + light + ";humidity:" + hum;
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
