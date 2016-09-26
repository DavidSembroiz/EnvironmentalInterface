import java.io.*;
import java.nio.file.*;

import org.json.simple.parser.*;
import org.json.simple.JSONObject;

public class modelGenerator {


    private static String modelPath = "./";
    private static String loc = "upc/campusnord/";
    private static File f;


    public static String getProperRoomName(int id) {
        String res = "";      

        if (id < 10) res = "d600" + id;
        else if (id < 100) res = "d60" + id;
        else if (id < 1000) res = "d6" + id;
        return res;
    }


    public static JSONObject insertCustomFields(int id, int sensor) {
        String custfield = "";
        try {
            String model = modelPath + "sensor_" + sensor + ".json";
            f = new File(model);
            if (f.exists()) {
                JSONParser parser = new JSONParser();
		        JSONObject obj = (JSONObject) parser.parse(new FileReader(f));
                JSONObject customFields = (JSONObject) obj.get("customFields");
                String mod = (String) customFields.get("model");
                customFields.put("location", loc + getProperRoomName(id));
                customFields.put("model", mod + "_0");
                return obj;
            }
            else {
                System.out.println("Unable to read model for sensor " + id);
            }
        } catch(IOException|ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void writeToFile(String content) {
	    try {
		    FileWriter fw = new FileWriter(f);
		    BufferedWriter bw = new BufferedWriter(fw);

		    bw.write(content);
		    bw.close();
	    }
	    catch (IOException e) {}
    }

    public static void writeObjectToFile(JSONObject obj) {
        if (obj != null) {
            writeToFile(obj.toJSONString());
        }
    }

    public static void createModel(int id, String mod) {
		try {
            Files.copy(Paths.get(modelPath + mod.toLowerCase() + ".json"),
                       Paths.get(modelPath + "sensor_" + id + ".json"), StandardCopyOption.REPLACE_EXISTING);
		} catch(IOException e) { e.printStackTrace(); }
	}
    

    public static void main(String argv[]) throws Exception {

        int rooms = 0;        

        if (argv.length == 1) {
            rooms = Integer.parseInt(argv[0]);
        }
        else {
            System.out.println("Usage: java modelGenerator #rooms");
            System.exit(1);
        }

        int sensors = 1;

        for (int i = 1; i <= rooms; ++i) {
            createModel(sensors, "xm1000");
            writeObjectToFile(insertCustomFields(i, sensors++));
            createModel(sensors, "light");
            writeObjectToFile(insertCustomFields(i, sensors++));
            createModel(sensors, "presence");
            writeObjectToFile(insertCustomFields(i, sensors++));
            createModel(sensors, "power");
            writeObjectToFile(insertCustomFields(i, sensors++));
            createModel(sensors, "airquality");
            writeObjectToFile(insertCustomFields(i, sensors++));
        }
    }
}
