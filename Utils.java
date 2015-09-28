import java.io.*;
import java.nio.file.*;

import org.json.simple.parser.*;
import org.json.simple.JSONObject;

public class Utils {

    //Specify the root path where the scripts and files are located
    private String ROOT_PATH = "servioticy/";
    private int id;
    private File f;
    private Database awsdb;
    private HttpConnection httpconn;


    public Utils(int source, Database awsdb) {
        this.id = source;
		this.awsdb = awsdb;
        this.httpconn = new HttpConnection();
    }

	public void createDatafile() {
		try {
            String model = readSOCustomField(id, "model");
            Files.copy(Paths.get(ROOT_PATH + "data/template_" + model.toLowerCase() + ".json"),
                       Paths.get(ROOT_PATH + "data/data_" + Long.toString(id) + ".json"), StandardCopyOption.REPLACE_EXISTING);
		} catch(IOException e) { 
            System.out.println("Template not found");
        }
	}

    // Checks for Service Object ID, if it does not exist, it creates the dependencies.
    public File openDatafile() {
        String soID = ROOT_PATH + "res/id" + id;
        String name = ROOT_PATH + "data/data_" + id + ".json";
        
        File so = new File(soID);
		f = new File(name);
        System.out.println("Opening " + name);
		if (!so.exists() || so.length() == 0) {
            System.out.println("ID file does not exist, creating dependencies...");
            create(f);
            createDatafile();
		}
		else {
			if (!f.exists() || f.length() == 0) {
				System.out.println("Copying datafile template...");
				createDatafile();
			}		
		}
        return f;
    }

    private void enableMQTT(int id) {

        String soID = ROOT_PATH + "res/id" + id;
        File so = new File(soID);

        if (so.exists()) {
            System.out.println("Enabling MQTT subscription for id " + id);
            httpconn.subscribe(soID);
        }
        else {
            System.out.println("Unable to create MQTT subscription for id " + id);
        }
    }

    // Creates a new SO in ServIoTicy
    private void create(File dat) {
            String model = ROOT_PATH + "models/sensor_" + id + ".json";
            String resID = ROOT_PATH + "res/id" + id;

            File f = new File(model);
            File res = new File(resID);

            if (f.exists()) {
                if (!res.exists() || res.length() == 0) {
                    
                    System.out.println("Creating SO... " + model);
                    if (httpconn.create(model, res)) {
                    
                        String soID = readSOid(id);
                        String mod = readSOCustomField(id, "model");
                        String location = readSOCustomField(id, "location");
                        String associations = readSOCustomField(id, "associations");
                        if (soID != null && mod != null && location != null) {
					        awsdb.insertSO(soID, mod, location, associations);
                            enableMQTT(id);
                        }
                    }
                    else {
                        System.out.println("Unable to create SO");                
                    }
                }
            }
            else {
                System.out.println("Unable to create Service Object for Mote " + id + ", model not found");    
            }
    }

    // Reads Service Object ID
    public String readSOid(int id) {
        String soID = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(ROOT_PATH + "res/id" + id));
		    soID = br.readLine();
		    br.close();
        } catch(IOException e) {
            e.printStackTrace();        
        }
        return soID;
    }

    // Reads Service Object Custom field specified in field parameter (model, location)
    public String readSOCustomField(int id, String field) {
        String custfield = "";
        try {
            String model = ROOT_PATH + "models/sensor_" + id + ".json";
            File f = new File(model);
            if (f.exists()) {
                JSONParser parser = new JSONParser();
		        JSONObject obj = (JSONObject) parser.parse(new FileReader(f));
                JSONObject customFields = (JSONObject) obj.get("customFields");
		        custfield = (String) customFields.get(field);
            }
            else {
                System.out.println("Unable to read model for sensor " + id);
            }
        } catch(IOException|ParseException e) {
            e.printStackTrace();
        }
        return custfield;
    }


    // Writes the new reading values to file, only the last update is saved
    public void writeToDataFile(String content) {
	    try {
		    FileWriter fw = new FileWriter(f);
		    BufferedWriter bw = new BufferedWriter(fw);

		    bw.write(content);
		    bw.close();
	    }
	    catch (IOException e) {}
    }

    public void pushData() {
        String soID = ROOT_PATH + "res/id" + id;
        String dataPath = ROOT_PATH + "data/data_" + id + ".json";
        File so = new File(soID);
        if (so.exists()) httpconn.push(soID, dataPath);
    }

    public void revertCreation() {
        String resID = ROOT_PATH + "res/id" + id;
        File so = new File(resID);
        if (so.exists()) so.delete();
    }
    
}
