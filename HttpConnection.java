import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class HttpConnection {
	
    private Properties prop;
    private String SERVIOTICY_URL;
	private String API_KEY;
    private Utils uts;


    public HttpConnection() {
        loadProperties();
    }

    private void loadProperties() {
        prop = new Properties();
        try {
            InputStream is = new FileInputStream("database.properties");
			prop.load(is);
			SERVIOTICY_URL = prop.getProperty("so_address");
			API_KEY = prop.getProperty("so_apikey");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	
	public void subscribe(String idFilename) {
		String id;
		try {
			id = readFileFromName(idFilename);
        
			URL url = new URL(SERVIOTICY_URL + id + "/streams/data/subscriptions");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", API_KEY);
			con.setDoOutput(true);
			con.setDoInput(true);
			
			String subs = readFileFromName("servioticy/res/subscribe.json");
			writeOutput(con.getOutputStream(), subs);
			
			String response = this.readHttpResponse(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean create(String model, File res) {
		URL url;
		try {
			url = new URL(SERVIOTICY_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", API_KEY);
			con.setDoOutput(true);
			con.setDoInput(true);
			
			String data = readFileFromName(model);
			
			writeOutput(con.getOutputStream(), data);
			String response = readHttpResponse(con.getInputStream());
			String id = parseID(response);
			writeToFile(res, id);
            return true;
		} catch (Exception e) {
            return false;
		}
	}
	
	public void push(String idFilename, String filename) {
		String id;
		try {
			id = readFileFromName(idFilename);
			URL obj = new URL(SERVIOTICY_URL + id + "/streams/data");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", API_KEY);
			con.setDoOutput(true);
			con.setDoInput(true);
			
			String data = readFileFromName(filename);
			
			writeOutput(con.getOutputStream(), data);

            String response = readHttpResponse(con.getInputStream());
            System.out.println(response);            

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String parseID(String response) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(response);
        String id = (String) obj.get("id");
        return id;
	}
	
	private String readHttpResponse(InputStream is) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String input = "";
		String response = "";
		while((input = in.readLine()) != null) {
			response += input;
		}
		in.close();
		return response;
	}
	
	private void writeOutput(OutputStream os, String data) throws Exception {
		DataOutputStream wr = new DataOutputStream(os);
		wr.writeBytes(data);
		wr.flush();
		wr.close();
	}
	
	private void writeToFile(File res, String response) throws Exception {
		OutputStream wr = new FileOutputStream(res);
		wr.write(response.getBytes());
		wr.close();
	}
	
	private String readFileFromName(String filename) throws Exception {
		FileReader f = new FileReader(filename);
		BufferedReader br = new BufferedReader(f);
		String line = "";
		String data = "";
		while ((line = br.readLine()) != null) {
			data += line;
		}
		br.close();
		return data;
	}

}

